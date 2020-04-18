package com.planr.solver.core

import com.google.ortools.constraintsolver._
import com.planr.api.enumeration.OperationRelationType
import com.planr.api.messages._
import com.planr.solver.config.CostConfig._
import com.planr.solver.config.SolverConfig
import play.api.Logger

import scala.util.Try

object PlanrSolver {
  def apply(): PlanrSolver = new PlanrSolver()
}

class PlanrSolver extends Solver("PlanrSolver") {

  private val logger = Logger(this.getClass)

  def search(problem: Problem, dayFrame: DayFrame, searchInterval: Long, solverConfig: SolverConfig): Option[SolverSolution] =
    Try {
      for {
        model    <- createModel(problem, dayFrame, searchInterval, solverConfig)
        solution <- solveModel(model)
      } yield solution
    }.fold(
      error => {
        logger.error(s"Failed solver search: ${error.getMessage}")
        None
      },
      value => value
    )

  private def createModel(problem: Problem, dayFrame: DayFrame, searchInterval: Long, solverConfig: SolverConfig): Option[SolverProblem] = {
    // Create Variance Domain
    val varianceDomain = createVarianceDomain(problem.operations, dayFrame.allocations, dayFrame.day)
    val intervals      = varianceDomain.map(_.interval)
    val sequences      = varianceDomain.map(_.sequence)
    val resources      = varianceDomain.map(_.resource)

    // Apply Constraints
    // Mandatory
    programConstraint(intervals, dayFrame.day, dayFrame.program)

    // Optional
    problem.constraints.foreach(constraints => {
      constraints.operationGrid.foreach(operationGridConstraint(intervals, _))
      constraints.sameResource.foreach(sameResourceConstraint(problem.operations, varianceDomain, _))
      constraints.enforcedTimeInterval.foreach(enforcedTimeIntervalConstraint(intervals, dayFrame.day, _))
      constraints.operationsRelation.foreach(operationsRelationConstraint(varianceDomain, _))
      ()
    })

    // Apply Costs
    // Optional
    val costs: Array[IntVar] = Array(
      makeProd(asSoonAsPossibleCost(problem.costs.flatMap(_.asSoonAsPossible), intervals, searchInterval), ASAP_PERCENTAGE).`var`(),
      makeProd(asTightAsPossibleCost(problem.costs.flatMap(_.asTightAsPossible), intervals, dayFrame.day), ATAP_PERCENTAGE).`var`(),
      makeProd(preferredTimeIntervalCost(problem.costs.flatMap(_.preferredTimeInterval), intervals, dayFrame.day), PTI_PERCENTAGE).`var`()
    )
    val costObjective = makeDiv(makeSum(costs), PERCENTAGE * PRECISION).`var`()

    // Create Monitors
    val (mainPhase, objective, collector) = createMonitors(sequences, resources, costObjective)

    // Return Model
    Some(SolverProblem(sequences, resources, mainPhase, objective, collector, makeTimeLimit(solverConfig.solverTimeout)))
  }

  private def solveModel(solverProblem: SolverProblem): Option[SolverSolution] =
    if (solve(solverProblem.mainPhase, solverProblem.objective, solverProblem.timeLimit, solverProblem.collector))
      Some(SolverSolution(solverProblem.sequences, solverProblem.resources, solverProblem.objective, solverProblem.collector))
    else None

  private def createVarianceDomain(operations: Array[Operation], allocations: Array[Allocation], day: DateTimeInterval) =
    operations.map(operation => {
      val interval = createInterval(day.startDt, day.stopDt, operation.duration, optional = false, operation.key)
      val sequence = makeDisjunctiveConstraint(Array(interval), "").makeSequenceVar()
      val optional = operation.resourceKeys.length > 1

      val resourceIntervals = operation.resourceKeys.map(resourceKey => {
        val resourceInterval = createInterval(day.startDt, day.stopDt, operation.duration, optional, resourceKey + " " + operation.key)
        // Resource interval to match operation interval
        addConstraint(makeIntervalVarRelation(resourceInterval, Solver.STAYS_IN_SYNC, interval))
        resourceInterval
      })

      // Array of IntVar representing a 0 or 1 value, meaning if the resource interval is performed or not
      val intervalsExpr = resourceIntervals.map(_.performedExpr().`var`)

      // IntVar based on affinities index interval
      val resourceVar = makeIntVar(0L, operation.resourceKeys.length.toLong - 1L)

      // Resource var can take only values which are 1, mapping the solution domain to the Array of IntVar
      addConstraint(makeMapDomain(resourceVar, intervalsExpr))

      // Operation interval can take solutions from the given resource intervals
      addConstraint(makeCover(resourceIntervals, interval))

      // Operation interval disjunctive with the allocated intervals of the chosen affinity/resource
      operation.resourceKeys.indices.foreach(resourceIndex => {
        val isPerformed = resourceVar.isEqual(resourceIndex.toLong)
        val resource    = operation.resourceKeys(resourceIndex)
        val resourceAllocationIntervals = allocations
          .find(_.resourceKey == resource)
          .map(
            _.intervals.map(interval => makeFixedDurationIntervalVar(makeIntConst(day.startDt + interval.startT), day.startDt + interval.stopT - interval.startT, isPerformed, ""))
          )
        resourceAllocationIntervals.map(value => addConstraint(makeDisjunctiveConstraint(value :+ interval, "")))
      })

      VarianceDomain(operation.key, interval, sequence, resourceVar)
    })

  private def createInterval(start: Long, stop: Long, duration: Long, optional: Boolean, key: String) =
    makeFixedDurationIntervalVar(start, stop, duration, optional, key)

  private def createMonitors(
    sequences:     Array[SequenceVar],
    resources:     Array[IntVar],
    costObjective: IntVar
  ): (DecisionBuilder, OptimizeVar, SolutionCollector) = {
    // Minimize cost
    val objective = makeMinimize(costObjective, 1L)
    // Resource Decision Builder
    val resourcePhase = makePhase(resources, Solver.CHOOSE_FIRST_UNBOUND, Solver.ASSIGN_MIN_VALUE)
    // Sequence Decision Builder
    val sequencePhase = makePhase(sequences, Solver.SEQUENCE_DEFAULT)
    // Cost Decision Builder
    val costPhase = makePhase(costObjective, Solver.CHOOSE_FIRST_UNBOUND, Solver.ASSIGN_MIN_VALUE)
    // Composed Decision Builder
    val mainPhase = compose(resourcePhase, sequencePhase, costPhase)

    // Solution Collector
    val collector = makeLastSolutionCollector
    collector.add(sequences)
    collector.add(resources)

    // Add also the start/stop times of sequences
    sequences.foreach(sequence => {
      val interval = sequence.interval(0)
      collector.add(interval.startExpr.`var`())
      collector.add(interval.endExpr.`var`())
    })

    (mainPhase, objective, collector)
  }

  private def programConstraint(intervals: Array[IntervalVar], day: DateTimeInterval, program: TimeInterval): Unit =
    intervals.foreach(interval => {
      addConstraint(makeGreaterOrEqual(interval.startExpr(), day.startDt + program.startT))
      addConstraint(makeLessOrEqual(interval.endExpr(), day.startDt + program.stopT))
    })

  private def operationGridConstraint(intervals: Array[IntervalVar], operationGrid: Long): Unit =
    intervals.foreach(interval => addConstraint(makeEquality(makeModulo(interval.startExpr(), operationGrid), 0L)))

  private def sameResourceConstraint(operations: Array[Operation], varianceDomain: Array[VarianceDomain], opKeyss: Array[Array[String]]): Unit =
    opKeyss.foreach(opKeys => {
      val resourceKeys    = operations.filter(operation => opKeys.contains(operation.key)).flatMap(_.resourceKeys).distinct
      val resourceKeysMap = resourceKeys.map(resourceKey => resourceKey -> makeBoolVar()).toMap
      opKeys.foreach(opKey => {
        val operation = operations.find(_.key == opKey).get                // Guarded by validation
        val resource  = varianceDomain.find(_.opKey == opKey).get.resource // Guarded by validation

        operation.resourceKeys.indices.foreach(resourceIndex => {
          val resourceKey = operation.resourceKeys(resourceIndex)
          addConstraint(
            makeIfThenElseCt(
              resource.isEqual(resourceIndex.toLong),
              makeIntConst(1L),
              resourceKeysMap(resourceKey),
              resourceKeysMap(resourceKey)
            )
          )
        })
      })
      addConstraint(makeEquality(makeSum(resourceKeysMap.values.toArray), 1L))
    })

  private def enforcedTimeIntervalConstraint(intervals: Array[IntervalVar], day: DateTimeInterval, timeInterval: TimeInterval): Unit =
    intervals.foreach(interval => {
      addConstraint(makeGreaterOrEqual(interval.startExpr(), day.startDt + timeInterval.startT))
      addConstraint(makeLessOrEqual(interval.endExpr(), day.startDt + timeInterval.stopT))
    })

  private def operationsRelationConstraint(varianceDomain: Array[VarianceDomain], operationsRelation: Array[OperationRelation]): Unit =
    operationsRelation.foreach(operationRelation => {
      val interval1 = varianceDomain.find(_.opKey == operationRelation.opKey1).get.interval // Guarded by validation
      val interval2 = varianceDomain.find(_.opKey == operationRelation.opKey2).get.interval // Guarded by validation
      operationRelation.opRelType match {
        case OperationRelationType.ENDS_AFTER_END     => addConstraint(makeIntervalVarRelation(interval1, Solver.ENDS_AFTER_END, interval2))
        case OperationRelationType.ENDS_AFTER_START   => addConstraint(makeIntervalVarRelation(interval1, Solver.ENDS_AFTER_START, interval2))
        case OperationRelationType.ENDS_AT_END        => addConstraint(makeIntervalVarRelation(interval1, Solver.ENDS_AT_END, interval2))
        case OperationRelationType.ENDS_AT_START      => addConstraint(makeIntervalVarRelation(interval1, Solver.ENDS_AT_START, interval2))
        case OperationRelationType.STARTS_AFTER_END   => addConstraint(makeIntervalVarRelation(interval1, Solver.STARTS_AFTER_END, interval2))
        case OperationRelationType.STARTS_AFTER_START => addConstraint(makeIntervalVarRelation(interval1, Solver.STARTS_AFTER_START, interval2))
        case OperationRelationType.STARTS_AT_END      => addConstraint(makeIntervalVarRelation(interval1, Solver.STARTS_AT_END, interval2))
        case OperationRelationType.STARTS_AT_START    => addConstraint(makeIntervalVarRelation(interval1, Solver.STARTS_AT_START, interval2))
      }
    })

  private def asSoonAsPossibleCost(asSoonAsPossible: Option[Boolean], intervals: Array[IntervalVar], searchInterval: Long): IntVar =
    if (asSoonAsPossible.isDefined && asSoonAsPossible.get)
      makeDiv(makeProd(makeMin(intervals.map(_.startExpr.`var`())), MAX_COST * PRECISION), searchInterval).`var`()
    else
      makeIntConst(0L)

  private def asTightAsPossibleCost(asTightAsPossible: Option[Boolean], intervals: Array[IntervalVar], day: DateTimeInterval): IntVar =
    if (asTightAsPossible.isDefined && asTightAsPossible.get)
      makeDiv(
        makeProd(makeDifference(makeMax(intervals.map(_.endExpr().`var`())), makeMin(intervals.map(_.startExpr.`var`()))), MAX_COST * PRECISION),
        day.stopDt - day.startDt
      ).`var`()
    else
      makeIntConst(0L)

  private def preferredTimeIntervalCost(preferredTimeInterval: Option[TimeInterval], intervals: Array[IntervalVar], day: DateTimeInterval): IntVar =
    if (preferredTimeInterval.isDefined) {
      val startDt                 = day.startDt + preferredTimeInterval.get.startT
      val stopDt                  = day.startDt + preferredTimeInterval.get.stopT
      val isPreferredTimeInterval = makeIntVar(0L, MAX_COST * PRECISION)

      addConstraint(
        makeIfThenElseCt(
          makeIsBetweenVar(makeMin(intervals.map(_.startExpr.`var`())), startDt, stopDt),
          makeIntConst(0L),
          makeIntConst(MAX_COST * PRECISION),
          isPreferredTimeInterval
        )
      )
      addConstraint(
        makeIfThenElseCt(
          makeIsBetweenVar(makeMax(intervals.map(_.endExpr().`var`())), startDt, stopDt),
          isPreferredTimeInterval,
          makeIntConst(MAX_COST * PRECISION),
          isPreferredTimeInterval
        )
      )

      isPreferredTimeInterval
    }
    else makeIntConst(0L)

}
