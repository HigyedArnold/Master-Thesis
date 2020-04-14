package com.planr.rest.test

import com.planr.api.messages.{Problems, Solutions}
import com.planr.rest.json.JsonSerializers._
import com.planr.rest.json.JsonUtil
import com.planr.solver.config.SolverConfig
import com.planr.solver.converter.SolutionConverter
import com.planr.solver.core.PlanrSolver
import com.planr.solver.util.NativeLibLoader
import org.scalatest.funsuite.AsyncFunSuite
import play.api.Logger
import play.api.libs.json.Json

class SolverTest extends AsyncFunSuite {

  private val logger = Logger(this.getClass)

  private val isSuccessfulInit = init()

  private def init(): Boolean =
    NativeLibLoader.init(NativeLibLoader.separator + "target" + NativeLibLoader.separator + "scala-2.13")

  private def solve(problems: Problems): Solutions = {
    assert(isSuccessfulInit)
    logger.debug(s"${Json.toJson(problems)}")
    val solver = PlanrSolver()
    val results = for {
      dayFrame       <- problems.dayFrames
      solverSolution <- solver.search(problems.problem, dayFrame, problems.searchInterval.getOrElse(1439L), SolverConfig(3000L, 0L))
      solution       <- SolutionConverter().convert(solverSolution, problems.problem, dayFrame)
    } yield solution
    val solutions = Solutions(results)
    logger.debug(s"${Json.toJson(solutions)}")
    solutions
  }

  // ***************************************************** TEST ***************************************************** //

  test("Solver - Base test") {
    JsonUtil.jsonFileToCaseClass[Problems]("jsons/Problems_BaseTest.json") match {
      case Left(_) =>
        fail()
      case Right(problems) =>
        val solutions = solve(problems)
        assert(solutions.solutions.length == 1)
        succeed
    }
  }

}
