package com.planr.rest.test

import com.planr.api.msg.Problems
import com.planr.rest.json.JsonUtil
import com.planr.rest.json.JsonSerializers._
import org.scalatest.funsuite.AsyncFunSuite
import play.api.Logger
import play.api.libs.json.Json

class JsonSerializersTest extends AsyncFunSuite {

  private val logger = Logger(this.getClass)

  test("Problems serialization test") {
    JsonUtil.jsonFileToCaseClass[Problems]("jsons/Problems1.json") match {
      case Left(_) =>
        fail()
      case Right(value) =>
        logger.info(s"${Json.toJson(value)}")
        succeed
    }
  }

}
