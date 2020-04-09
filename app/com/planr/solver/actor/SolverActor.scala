package com.planr.solver.actor

import akka.actor.Actor
import com.planr.api.messages.{DayFrame, Error, Problem, Solution}
import com.planr.api.messages.ErrorCodes._
import com.planr.solver.DisjunctiveIntervals
import com.planr.solver.actor.SolverActor.SolveRequest
import com.planr.solver.config.SolverConfig
import play.api.Logger

object SolverActor {
  case class SolveRequest(problem:   Problem, dayFrame: DayFrame, solverConfig: SolverConfig)
  case class SolveResponse(solution: Solution)
}

class SolverActor extends Actor {

  private val logger = Logger(this.getClass)

  override def receive: Receive = {
    case SolveRequest(problem: Problem, dayFrame: DayFrame, solverConfig: SolverConfig) =>
      // Test
      val disjunctiveIntervals = new DisjunctiveIntervals
      disjunctiveIntervals.search(0, 10, disjunctiveIntervals.blockedIntervals1, disjunctiveIntervals.requestIntervals1)

      sender ! Right(None)
    case error =>
      val err = Error(this.getClass.getName, SOVLER__ERROR + UNKNOWN_ACTOR_MESSAGE__ERROR, s"SolverActor received unknown message: ${error.toString}")
      logger.error(err.toString)
      sender ! Left(err)
  }
}
