package com.planr.gatling.test

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.request.builder.HttpRequestBuilder

object BaseScenarios {

  /** -------------------------------------------------- Variables -------------------------------------------------- */

  /** ---------------------------------------------------- HTTPs ---------------------------------------------------- */
  val testHttp: HttpRequestBuilder = http("Planr: /api")
    .get("/api")
    .check(status.is(200))

  /** -------------------------------------------------- Scenarios -------------------------------------------------- */
  val testScenario: ScenarioBuilder = scenario("GET /api")
    .exec(testHttp)

}
