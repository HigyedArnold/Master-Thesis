package com.planr.rest.test

import com.planr.api.msg.Problems
import com.planr.rest.json.JsonUtil
import com.planr.rest.json.JsonSerializers._
import org.scalatest.funsuite.AsyncFunSuite
import org.slf4j.LoggerFactory
import play.api.libs.json.Json

class JsonSerializersTest extends AsyncFunSuite {

  private val logger = LoggerFactory.getLogger(this.getClass)

  test("Problem serialization test") {
    JsonUtil.jsonFileToCaseClass[Problems]("jsons/Problem1.json") match {
      case Left(error) =>
        logger.error(s"${Json.toJson(error)}")
        fail()
      case Right(value) =>
        logger.info(s"${Json.toJson(value)}")
        succeed
    }
  }

}
