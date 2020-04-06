package com.planr.api.test

import org.scalatest.funsuite.AsyncFunSuite
import com.planr.api.JsonUtil._
import com.planr.api.{Problems, TestRequest, TestResponse}
import com.planr.api.JsonSerializers._
import org.slf4j.{Logger, LoggerFactory}
import play.api.libs.json.Json

class JsonSerializersTest extends AsyncFunSuite {

  val logger: Logger = LoggerFactory.getLogger(this.getClass)

  test("TestRequest serialization test") {
    jsonFileToCaseClass[TestRequest]("/jsons/TestRequest.json") match {
      case Left(error) =>
        logger.error(s"${Json.toJson(error)}")
        fail()
      case Right(value) =>
        logger.info(s"${Json.toJson(value)}")
        succeed
    }
  }

  test("TestResponse serialization test") {
    jsonFileToCaseClass[TestResponse]("/jsons/TestResponse.json") match {
      case Left(error) =>
        logger.error(s"${Json.toJson(error)}")
        fail()
      case Right(value) =>
        logger.info(s"${Json.toJson(value)}")
        succeed
    }
  }

  test("Problem serialization test") {
    jsonFileToCaseClass[Problems]("/jsons/Problem1.json") match {
      case Left(error) =>
        logger.error(s"${Json.toJson(error)}")
        fail()
      case Right(value) =>
        logger.info(s"${Json.toJson(value)}")
        succeed
    }
  }

//  test("Solutions serialization test") {
//    jsonFileToCaseClass[Solutions]("/jsons/Solutions1.json") match {
//      case Left(error) =>
//        logger.error(s"${Json.toJson(error)}")
//        fail()
//      case Right(value) =>
//        logger.info(s"${Json.toJson(value)}")
//        succeed
//    }
//  }
}
