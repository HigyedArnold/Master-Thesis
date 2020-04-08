package com.planr.api.async

import com.planr.api.effects.FutureResult
import com.planr.api.messages.{Problems, Solutions}

trait SolverServiceT {
  def solve(problems: Problems): FutureResult[Solutions]
}

object SolverServiceT {
  val actorName = "solverActor"
  val actorPath = s"akka://application/user/$actorName"
}
