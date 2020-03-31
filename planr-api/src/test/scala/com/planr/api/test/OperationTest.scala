package com.planr.api.test

import org.scalatest.funsuite.AsyncFunSuite
import com.planr.api.JsonUtil._
import com.planr.api.Operation
import com.planr.api.JsonSerializers._
import org.slf4j.{Logger, LoggerFactory}

class OperationTest extends AsyncFunSuite {

  val logger: Logger = LoggerFactory.getLogger(this.getClass)

  test("Operation serialization test") {
    jsonFileToCaseClass[Operation]("/jsons/operation1.json") match {
      case Left(error) =>
        logger.error(error.toString)
        fail()
      case Right(value) =>
        logger.debug(value.toString)
        succeed
    }
  }
}
