package com.planr.solver.actor

import akka.actor.Actor
import com.planr.api.messages.ErrorCodes._
import com.planr.api.messages.{DayFrame, Error, Problem, Solution}
import com.planr.solver.actor.SolverActor.SolveRequest
import com.planr.solver.config.SolverConfig
import com.planr.solver.converter.SolutionConverter
import com.planr.solver.core.PlanrSolver
import play.api.Logger

import scala.util.Try

object SolverActor {
  case class SolveRequest(problem:   Problem, dayFrame: DayFrame, searchInterval: Long, solverConfig: SolverConfig)
  case class SolveResponse(solution: Solution)
}

class SolverActor extends Actor {

  private val logger = Logger(this.getClass)

  override def receive: Receive = {
    case SolveRequest(problem: Problem, dayFrame: DayFrame, searchInterval: Long, solverConfig: SolverConfig) =>
      val solver = PlanrSolver()
      val result = for {
        solverSolution <- solver.search(problem, dayFrame, searchInterval, solverConfig)
        solution       <- SolutionConverter().convert(solverSolution, problem, dayFrame)
      } yield solution
      sender ! Right(result)
      // Cleanup
      Try {
        solver.delete()
      }.fold(
        error => logger.error(s"Failed solver cleanup: ${error.getMessage}"),
        _ => ()
      )
    case error =>
      val err = Error(this.getClass.getName, SOVLER__ERROR + UNKNOWN_ACTOR_MESSAGE__ERROR, s"SolverActor received unknown message: ${error.toString}")
      logger.error(err.toString)
      sender ! Left(err)
  }
}
