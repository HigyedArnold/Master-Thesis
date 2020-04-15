package com.planr.rest.test

import com.planr.api.messages.{Problems, Solution, Solutions}
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
    val validation = problems.validate
    if (problems.validate.isRight) {
      logger.debug(s"${Json.toJson(problems)}")
      val solver = PlanrSolver()
      val results = for {
        dayFrame       <- problems.dayFrames
        solverSolution <- solver.search(problems.problem, dayFrame, problems.searchInterval.getOrElse(problems.dayFrames.last.day.stopDt), SolverConfig(3000L, 0L))
        solution       <- SolutionConverter().convert(solverSolution, problems.problem, dayFrame)
      } yield solution
      val solutions = Solutions(results)
      logger.debug(s"${Json.toJson(solutions)}")
      solutions
    }
    else {
      logger.error(validation.left.toString)
      Solutions(Array.empty[Solution])
    }
  }

  // *********************************************** VALIDATION  TEST *********************************************** //

  //                                                     General                                                      //

  test("Solver - No resource key for operation") {
    assert(isSuccessfulInit)
    JsonUtil.jsonFileToCaseClass[Problems]("jsons/NoResourceKey.json") match {
      case Left(_) =>
        fail()
      case Right(problems) =>
        val solutions = solve(problems)
        assert(solutions.solutions.length == 0)
    }
  }

  test("Solver - No day frame") {
    assert(isSuccessfulInit)
    JsonUtil.jsonFileToCaseClass[Problems]("jsons/NoDayFrame.json") match {
      case Left(_) =>
        fail()
      case Right(problems) =>
        val solutions = solve(problems)
        assert(solutions.solutions.length == 0)
    }
  }

  //                                                    Resources                                                     //

  test("Solver - No resource for key") {
    assert(isSuccessfulInit)
    JsonUtil.jsonFileToCaseClass[Problems]("jsons/NoResourceForKey.json") match {
      case Left(_) =>
        fail()
      case Right(problems) =>
        val solutions = solve(problems)
        assert(solutions.solutions.length == 0)
    }
  }

  //                                                   Constraints                                                    //

  test("Solver - Invalid operation grid") {
    assert(isSuccessfulInit)
    JsonUtil.jsonFileToCaseClass[Problems]("jsons/InvalidOperationGrid.json") match {
      case Left(_) =>
        fail()
      case Right(problems) =>
        val solutions = solve(problems)
        assert(solutions.solutions.length == 0)
    }
  }

  test("Solver - Invalid same resource 1") {
    assert(isSuccessfulInit)
    JsonUtil.jsonFileToCaseClass[Problems]("jsons/InvalidSameResource1.json") match {
      case Left(_) =>
        fail()
      case Right(problems) =>
        val solutions = solve(problems)
        assert(solutions.solutions.length == 0)
    }
  }

  test("Solver - Invalid same resource 2") {
    assert(isSuccessfulInit)
    JsonUtil.jsonFileToCaseClass[Problems]("jsons/InvalidSameResource2.json") match {
      case Left(_) =>
        fail()
      case Right(problems) =>
        val solutions = solve(problems)
        assert(solutions.solutions.length == 0)
    }
  }

  test("Solver - Invalid same resource 3") {
    assert(isSuccessfulInit)
    JsonUtil.jsonFileToCaseClass[Problems]("jsons/InvalidSameResource3.json") match {
      case Left(_) =>
        fail()
      case Right(problems) =>
        val solutions = solve(problems)
        assert(solutions.solutions.length == 0)
    }
  }

  test("Solver - Invalid enforced time interval 1") {
    assert(isSuccessfulInit)
    JsonUtil.jsonFileToCaseClass[Problems]("jsons/InvalidEnforcedTimeInterval1.json") match {
      case Left(_) =>
        fail()
      case Right(problems) =>
        val solutions = solve(problems)
        assert(solutions.solutions.length == 0)
    }
  }

  test("Solver - Invalid enforced time interval 2") {
    assert(isSuccessfulInit)
    JsonUtil.jsonFileToCaseClass[Problems]("jsons/InvalidEnforcedTimeInterval2.json") match {
      case Left(_) =>
        fail()
      case Right(problems) =>
        val solutions = solve(problems)
        assert(solutions.solutions.length == 0)
    }
  }

  test("Solver - Invalid enforced time interval 3") {
    assert(isSuccessfulInit)
    JsonUtil.jsonFileToCaseClass[Problems]("jsons/InvalidEnforcedTimeInterval3.json") match {
      case Left(_) =>
        fail()
      case Right(problems) =>
        val solutions = solve(problems)
        assert(solutions.solutions.length == 0)
    }
  }

  test("Solver - Invalid operations relation 1") {
    assert(isSuccessfulInit)
    JsonUtil.jsonFileToCaseClass[Problems]("jsons/InvalidOperationsRelation1.json") match {
      case Left(_) =>
        fail()
      case Right(problems) =>
        val solutions = solve(problems)
        assert(solutions.solutions.length == 0)
    }
  }

  test("Solver - Invalid operations relation 2") {
    assert(isSuccessfulInit)
    JsonUtil.jsonFileToCaseClass[Problems]("jsons/InvalidOperationsRelation2.json") match {
      case Left(_) =>
        fail()
      case Right(problems) =>
        val solutions = solve(problems)
        assert(solutions.solutions.length == 0)
    }
  }

  //                                                      Costs                                                       //

  test("Solver - Invalid preferred time interval 1") {
    assert(isSuccessfulInit)
    JsonUtil.jsonFileToCaseClass[Problems]("jsons/InvalidPreferredTimeInterval1.json") match {
      case Left(_) =>
        fail()
      case Right(problems) =>
        val solutions = solve(problems)
        assert(solutions.solutions.length == 0)
    }
  }

  test("Solver - Invalid preferred time interval 2") {
    assert(isSuccessfulInit)
    JsonUtil.jsonFileToCaseClass[Problems]("jsons/InvalidPreferredTimeInterval2.json") match {
      case Left(_) =>
        fail()
      case Right(problems) =>
        val solutions = solve(problems)
        assert(solutions.solutions.length == 0)
    }
  }

  test("Solver - Invalid preferred time interval 3") {
    assert(isSuccessfulInit)
    JsonUtil.jsonFileToCaseClass[Problems]("jsons/InvalidPreferredTimeInterval3.json") match {
      case Left(_) =>
        fail()
      case Right(problems) =>
        val solutions = solve(problems)
        assert(solutions.solutions.length == 0)
    }
  }

  // *************************************************** BZL TEST *************************************************** //

  //                                                      Base                                                        //

  test("Solver - Base test") {
    assert(isSuccessfulInit)
    JsonUtil.jsonFileToCaseClass[Problems]("jsons/Base.json") match {
      case Left(_) =>
        fail()
      case Right(problems) =>
        val solutions = solve(problems)
        assert(solutions.solutions.length == 1)
    }
  }

  //                                                   Performance                                                    //

  test("Solver - Performance test") {
    assert(isSuccessfulInit)
    JsonUtil.jsonFileToCaseClass[Problems]("jsons/Performance.json") match {
      case Left(_) =>
        fail()
      case Right(problems) =>
        val solutions = solve(problems)
        assert(solutions.solutions.length == 5)
    }
  }

}
