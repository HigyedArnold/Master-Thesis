package com.planr.rest.impl

import akka.actor.{ActorRef, ActorSystem}
import akka.pattern.ask
import com.google.inject.Inject
import com.planr.api.async.SolverServiceT
import com.planr.api.effects.FutureResult
import com.planr.api.messages.{Error, Problems, Solution, Solutions}
import com.planr.solver.config.SolverConfig
import play.api.{Configuration, Logger}
import com.planr.api.effects.implicits._
import cats.implicits._
import com.planr.solver.actor.SolverActor

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration._

class SolverService @Inject() (config: Configuration, actorSystem: ActorSystem)(implicit ec: ExecutionContext) extends SolverServiceT {

  private val logger = Logger(this.getClass)
  private val MILLIS = 1000000

  override def solve(problems: Problems): FutureResult[Solutions] =
    (for {
      /** Config */
      solverConfig <- readConfig().asPureFRT
      /** Actors */
      solverActor <- actorSystem.actorSelection(SolverServiceT.actorPath).resolveOne()(solverConfig.actorTimeout.seconds).map(Right(_)).asFRT
      /** Solve */
      t0        <- System.nanoTime().asPureFRT
      solutions <- getSolutions(problems, solverConfig, solverActor).asFRT
      t1        <- System.nanoTime().asPureFRT
      _         <- logger.debug(s"Elapsed time for solving problems: ${(t1 - t0) / MILLIS} ms").asPureFRT

    } yield solutions).value

  private def readConfig(): SolverConfig =
    SolverConfig(config.get[Int]("solver.actor.timeout"))

  private def getSolutions(problems: Problems, solverConfig: SolverConfig, solverActor: ActorRef): FutureResult[Solutions] = {
    for {
      responses <- Future
        .traverse(problems.dayFrames.toList)(dayFrame => solverActor.ask(SolverActor.SolveRequest(problems.problem, dayFrame, solverConfig))(solverConfig.actorTimeout.seconds))
        .mapTo[List[Either[Error, Option[Solution]]]]
      solutions <- responses
        .foldLeft(Right(List.empty[Solution]): Either[Error, List[Solution]]) { (acc, elem) =>
          acc.flatMap(list => elem.map(list ++ _))
        }
        .map(solutions => Solutions(solutions.toArray))
        .pure[Future]
    } yield solutions
  }

}