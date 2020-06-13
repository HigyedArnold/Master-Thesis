package com.planr.solver.converter

import com.planr.api.messages._
import com.planr.solver.config.CostConfig._
import com.planr.solver.core.SolverSolution
import play.api.Logger

import scala.util.Try

object SolutionConverter {
  def apply(): SolutionConverter = new SolutionConverter()
}

class SolutionConverter {

  private val logger = Logger(this.getClass)

  def convert(solverSolution: SolverSolution, problem: Problem, dayFrame: DayFrame): Option[Solution] =
    Try {
      val operations = solverSolution.intervals.indices.toArray.map(index => {
        val operation = problem.operations(index)
        val interval  = solverSolution.intervals(index)

        val resourceIndex = solverSolution.collector.value(0, solverSolution.resources(index))
        val resource      = operation.resourceKeys(resourceIndex.toInt)
        val startDt       = solverSolution.collector.startValue(0, interval)
        val stopDt        = solverSolution.collector.endValue(0, interval)

        SolutionOperation(
          operation.key,
          operation.name,
          operation.duration,
          problem.resources.find(_.key == resource).get, // Guarded by validation
          DateTimeInterval(startDt, stopDt)
        )
      })

      val intervals = operations.map(_.interval)
      val interval  = DateTimeInterval(intervals.minBy(_.startDt).startDt, intervals.maxBy(_.stopDt).stopDt)
      val cost      = solverSolution.objective.best().toDouble / PRECISION.toDouble

      Solution(
        cost,
        interval.stopDt - interval.startDt,
        dayFrame.day,
        interval,
        operations
      )
    }.fold(
      error => {
        logger.error(s"Failed solver solution conversion: ${error.getMessage}")
        None
      },
      value => Some(value)
    )
}
