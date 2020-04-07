package com.planr.gatling.test

import io.gatling.core.Predef._
import io.gatling.core.scenario.Simulation
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

import scala.concurrent.duration._
import scala.language.postfixOps

class GatlingSpec extends Simulation {

  val httpConf: HttpProtocolBuilder = http.baseUrl("http://localhost:9000")

  // For options visit: https://gatling.io/docs/3.3/cheat-sheet/
  val scenarios = List(
    BaseScenarios.testScenario.inject(
      constantUsersPerSec(10).during(20 seconds)
    )
  )

  setUp(scenarios)
    .protocols(httpConf)
    .maxDuration(25 seconds)
    .assertions(global.responseTime.max.lte(100))

}