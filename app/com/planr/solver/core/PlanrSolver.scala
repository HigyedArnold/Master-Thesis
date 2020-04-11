package com.planr.solver.core

import com.google.ortools.constraintsolver._
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

  def search(problem: Problem, dayFrame: DayFrame, solverConfig: SolverConfig): Option[SolverSolution] =
    Try {
      for {
        model    <- createModel(problem, dayFrame, solverConfig)
        solution <- solveModel(model)
      } yield solution
    }.fold(
      error => {
        logger.error(s"Failed solver search: ${error.getMessage}")
        None
      },
      value => value
    )

  private def createModel(problem: Problem, dayFrame: DayFrame, solverConfig: SolverConfig): Option[SolverProblem] = {
    // Create Variance Domain
    val varianceDomain = createVarianceDomain(problem.operations, dayFrame.allocations, dayFrame.day)
    val intervals      = varianceDomain.map(_.interval)
    val sequences      = varianceDomain.map(_.sequence)
    val resources      = varianceDomain.map(_.resource)

    // Apply Constraints
    // Mandatory
    programConstraint(intervals, dayFrame.day, dayFrame.program)

    // Optional

    // Apply Costs
    // Optional
    val costs: Array[IntVar] = Array(
      makeProd(asSoonAsPossibleCost(intervals, dayFrame.day), ASAP_PERCENTAGE).`var`()
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
          .map(_.intervals.map(interval => makeFixedDurationIntervalVar(makeIntConst(interval.startDt), interval.stopDt - interval.startDt, isPerformed, "")))
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

  private def asSoonAsPossibleCost(intervals: Array[IntervalVar], day: DateTimeInterval): IntVar =
    makeDiv(makeProd(makeDifference(makeMin(intervals.map(_.startExpr.`var`())), makeIntConst(day.startDt)), MAX_COST * PRECISION), day.stopDt - day.startDt).`var`()

}
