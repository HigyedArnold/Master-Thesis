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

class PlanrTests extends AsyncFunSuite {

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

  // *********************************************** VALIDATION TESTS *********************************************** //

  //                                                     General                                                      //

  test("Solver - No resource key for operation") {
    assert(isSuccessfulInit)
    JsonUtil.jsonFileToCaseClass[Problems]("jsons/validation/NoResourceKey.json") match {
      case Left(_) =>
        fail()
      case Right(problems) =>
        val solutions = solve(problems)
        assert(solutions.solutions.length == 0)
    }
  }

  test("Solver - No day frame") {
    assert(isSuccessfulInit)
    JsonUtil.jsonFileToCaseClass[Problems]("jsons/validation/NoDayFrame.json") match {
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
    JsonUtil.jsonFileToCaseClass[Problems]("jsons/validation/NoResourceForKey.json") match {
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
    JsonUtil.jsonFileToCaseClass[Problems]("jsons/validation/InvalidOperationGrid.json") match {
      case Left(_) =>
        fail()
      case Right(problems) =>
        val solutions = solve(problems)
        assert(solutions.solutions.length == 0)
    }
  }

  test("Solver - Invalid same resource 1") {
    assert(isSuccessfulInit)
    JsonUtil.jsonFileToCaseClass[Problems]("jsons/validation/InvalidSameResource1.json") match {
      case Left(_) =>
        fail()
      case Right(problems) =>
        val solutions = solve(problems)
        assert(solutions.solutions.length == 0)
    }
  }

  test("Solver - Invalid same resource 2") {
    assert(isSuccessfulInit)
    JsonUtil.jsonFileToCaseClass[Problems]("jsons/validation/InvalidSameResource2.json") match {
      case Left(_) =>
        fail()
      case Right(problems) =>
        val solutions = solve(problems)
        assert(solutions.solutions.length == 0)
    }
  }

  test("Solver - Invalid same resource 3") {
    assert(isSuccessfulInit)
    JsonUtil.jsonFileToCaseClass[Problems]("jsons/validation/InvalidSameResource3.json") match {
      case Left(_) =>
        fail()
      case Right(problems) =>
        val solutions = solve(problems)
        assert(solutions.solutions.length == 0)
    }
  }

  test("Solver - Invalid enforced time interval 1") {
    assert(isSuccessfulInit)
    JsonUtil.jsonFileToCaseClass[Problems]("jsons/validation/InvalidEnforcedTimeInterval1.json") match {
      case Left(_) =>
        fail()
      case Right(problems) =>
        val solutions = solve(problems)
        assert(solutions.solutions.length == 0)
    }
  }

  test("Solver - Invalid enforced time interval 2") {
    assert(isSuccessfulInit)
    JsonUtil.jsonFileToCaseClass[Problems]("jsons/validation/InvalidEnforcedTimeInterval2.json") match {
      case Left(_) =>
        fail()
      case Right(problems) =>
        val solutions = solve(problems)
        assert(solutions.solutions.length == 0)
    }
  }

  test("Solver - Invalid enforced time interval 3") {
    assert(isSuccessfulInit)
    JsonUtil.jsonFileToCaseClass[Problems]("jsons/validation/InvalidEnforcedTimeInterval3.json") match {
      case Left(_) =>
        fail()
      case Right(problems) =>
        val solutions = solve(problems)
        assert(solutions.solutions.length == 0)
    }
  }

  test("Solver - Invalid operations relation 1") {
    assert(isSuccessfulInit)
    JsonUtil.jsonFileToCaseClass[Problems]("jsons/validation/InvalidOperationsRelation1.json") match {
      case Left(_) =>
        fail()
      case Right(problems) =>
        val solutions = solve(problems)
        assert(solutions.solutions.length == 0)
    }
  }

  test("Solver - Invalid operations relation 2") {
    assert(isSuccessfulInit)
    JsonUtil.jsonFileToCaseClass[Problems]("jsons/validation/InvalidOperationsRelation2.json") match {
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
    JsonUtil.jsonFileToCaseClass[Problems]("jsons/validation/InvalidPreferredTimeInterval1.json") match {
      case Left(_) =>
        fail()
      case Right(problems) =>
        val solutions = solve(problems)
        assert(solutions.solutions.length == 0)
    }
  }

  test("Solver - Invalid preferred time interval 2") {
    assert(isSuccessfulInit)
    JsonUtil.jsonFileToCaseClass[Problems]("jsons/validation/InvalidPreferredTimeInterval2.json") match {
      case Left(_) =>
        fail()
      case Right(problems) =>
        val solutions = solve(problems)
        assert(solutions.solutions.length == 0)
    }
  }

  test("Solver - Invalid preferred time interval 3") {
    assert(isSuccessfulInit)
    JsonUtil.jsonFileToCaseClass[Problems]("jsons/validation/InvalidPreferredTimeInterval3.json") match {
      case Left(_) =>
        fail()
      case Right(problems) =>
        val solutions = solve(problems)
        assert(solutions.solutions.length == 0)
    }
  }

  // ************************************************* SOLVER TESTS ************************************************* //

  //                                                      Base                                                        //

  test("Solver - Base test") {
    assert(isSuccessfulInit)
    JsonUtil.jsonFileToCaseClass[Problems]("jsons/solver/Base.json") match {
      case Left(_) =>
        fail()
      case Right(problems) =>
        val solutions = solve(problems)
        assert(solutions.solutions.length == 1)
    }
  }

  test("Solver - Respect day test") {
    assert(isSuccessfulInit)
    JsonUtil.jsonFileToCaseClass[Problems]("jsons/solver/RespectDay.json") match {
      case Left(_) =>
        fail()
      case Right(problems) =>
        val solutions = solve(problems)
        assert(solutions.solutions.length == 1)
        assert(solutions.solutions.head.interval.isBetween(0L, 1439L))
    }
  }

  test("Solver - Respect program test") {
    assert(isSuccessfulInit)
    JsonUtil.jsonFileToCaseClass[Problems]("jsons/solver/RespectProgram.json") match {
      case Left(_) =>
        fail()
      case Right(problems) =>
        val solutions = solve(problems)
        assert(solutions.solutions.length == 1)
        assert(solutions.solutions.head.interval.isBetween(480L, 540L))
    }
  }

  test("Solver - Respect allocations test") {
    assert(isSuccessfulInit)
    JsonUtil.jsonFileToCaseClass[Problems]("jsons/solver/RespectAllocations.json") match {
      case Left(_) =>
        fail()
      case Right(problems) =>
        val solutions = solve(problems)
        assert(solutions.solutions.length == 1)
        assert(solutions.solutions.head.interval.isBetween(500L, 540L))
    }
  }

  test("Solver - No cost test") {
    assert(isSuccessfulInit)
    JsonUtil.jsonFileToCaseClass[Problems]("jsons/solver/NoCost.json") match {
      case Left(_) =>
        fail()
      case Right(problems) =>
        val solutions = solve(problems)
        assert(solutions.solutions.length == 1)
        assert(solutions.solutions.head.cost == 0.0)
    }
  }

  test("Solver - Respect ASAP cost test") {
    assert(isSuccessfulInit)
    JsonUtil.jsonFileToCaseClass[Problems]("jsons/solver/RespectASAPCost.json") match {
      case Left(_) =>
        fail()
      case Right(problems) =>
        val solutions = solve(problems)
        assert(solutions.solutions.length == 1)
        assert(solutions.solutions.head.cost == 16.67)
    }
  }

  test("Solver - Respect ATAP cost test") {
    assert(isSuccessfulInit)
    JsonUtil.jsonFileToCaseClass[Problems]("jsons/solver/RespectATAPCost.json") match {
      case Left(_) =>
        fail()
      case Right(problems) =>
        val solutions = solve(problems)
        assert(solutions.solutions.length == 1)
        assert(solutions.solutions.head.cost == 17.89)                                       // ASAP is true in order to prevent the operations to be done in parallel
        assert(solutions.solutions.head.operations.map(_.resource.key).distinct.length == 2) // Operation2 done by Resource2, since Resource1 is blocked
    }
  }

  test("Solver - Respect PTI cost test 1") {
    assert(isSuccessfulInit)
    JsonUtil.jsonFileToCaseClass[Problems]("jsons/solver/RespectPTICost1.json") match {
      case Left(_) =>
        fail()
      case Right(problems) =>
        val solutions = solve(problems)
        assert(solutions.solutions.length == 1)
        assert(solutions.solutions.head.cost == 0.0)
        assert(solutions.solutions.head.interval.isBetween(540L, 600L))
    }
  }

  test("Solver - Respect PTI cost test 2") {
    assert(isSuccessfulInit)
    JsonUtil.jsonFileToCaseClass[Problems]("jsons/solver/RespectPTICost2.json") match {
      case Left(_) =>
        fail()
      case Right(problems) =>
        val solutions = solve(problems)
        assert(solutions.solutions.length == 1)
        assert(solutions.solutions.head.cost == 25.0) // Preferred interval blocked for Resource1
        assert(!solutions.solutions.head.interval.isBetween(540L, 600L))
    }
  }

  // *********************************************** PERFORMANCE TESTS ********************************************** //

  test("Solver - Performance test") {
    assert(isSuccessfulInit)
    JsonUtil.jsonFileToCaseClass[Problems]("jsons/performance/Performance.json") match {
      case Left(_) =>
        fail()
      case Right(problems) =>
        val solutions = solve(problems)
        assert(solutions.solutions.length == 5)
    }
  }

}
