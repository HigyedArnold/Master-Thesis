package com.planr.api.test

import org.scalatest.funsuite.AsyncFunSuite
import com.planr.api.JsonUtil._
import com.planr.api.Problem
import com.planr.api.JsonSerializers._
import org.slf4j.{Logger, LoggerFactory}
import play.api.libs.json.Json

class JsonSerializersTest extends AsyncFunSuite {

  val logger: Logger = LoggerFactory.getLogger(this.getClass)

  test("Problem serialization test") {
    jsonFileToCaseClass[Problem]("/jsons/Problem1.json") match {
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
