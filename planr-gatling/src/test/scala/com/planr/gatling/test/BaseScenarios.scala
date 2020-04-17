package com.planr.gatling.test

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.request.builder.HttpRequestBuilder

object BaseScenarios {

  /** -------------------------------------------------- Variables -------------------------------------------------- */

  val solveRequest = """{"problem":{"key":"Base","operations":[{"key":"Operation1","name":"Operation 1","duration":30,"resourceKeys":["Resource1","Resource3"]},{"key":"Operation2","name":"Operation 2","duration":30,"resourceKeys":["Resource2","Resource3"]},{"key":"Operation3","name":"Operation 3","duration":30,"resourceKeys":["Resource4","Resource3"]}],"resources":[{"key":"Resource1","name":"Resource 1"},{"key":"Resource2","name":"Resource 2"},{"key":"Resource3","name":"Resource 3"},{"key":"Resource4","name":"Resource 4"}],"costs":{"asSoonAsPossible":true,"asTightAsPossible":true,"preferredTimeInterval":{"startT":480,"stopT":660}},"constraints":{"operationGrid":5,"sameResource":[["Operation1","Operation2","Operation3"]],"operationsRelation":[{"opRelType":"STARTS_AFTER_END","opKey1":"Operation2","opKey2":"Operation1"},{"opRelType":"STARTS_AFTER_END","opKey1":"Operation3","opKey2":"Operation2"}]}},"dayFrames":[{"day":{"startDt":0,"stopDt":1439},"program":{"startT":480,"stopT":960},"allocations":[]},{"day":{"startDt":1440,"stopDt":2879},"program":{"startT":480,"stopT":960},"allocations":[]},{"day":{"startDt":2880,"stopDt":4319},"program":{"startT":480,"stopT":960},"allocations":[]},{"day":{"startDt":4320,"stopDt":5759},"program":{"startT":480,"stopT":960},"allocations":[]},{"day":{"startDt":5760,"stopDt":7200},"program":{"startT":480,"stopT":960},"allocations":[]}]}"""

  /** ---------------------------------------------------- HTTPs ---------------------------------------------------- */
  val testHttp: HttpRequestBuilder = http("Planr: /test")
    .get("/test")
    .check(status.is(200))

  val solveHttp: HttpRequestBuilder = http("Planr: /solve")
    .post("/solve")
    .header("Content-Type", "application/json")
    .body(StringBody(solveRequest))
    .check(status.is(200))
    .check(jsonPath("$.solutions..cost").count.is(5))

  /** -------------------------------------------------- Scenarios -------------------------------------------------- */
  val testScenario: ScenarioBuilder = scenario("GET /test")
    .exec(testHttp)

  val solveScenario: ScenarioBuilder = scenario("POST /solve")
    .exec(solveHttp)

}
