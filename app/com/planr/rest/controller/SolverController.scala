package com.planr.rest.controller

import com.google.inject.Inject
import com.planr.api.async.SolverServiceT
import com.planr.api.messages.{Problems, Solutions}
import com.planr.rest.api.{RestController, RestControllerComponents}
import com.planr.rest.json.JsonSerializers._
import play.api.Logger
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.Action

import scala.concurrent.ExecutionContext

class SolverController @Inject() (cc: RestControllerComponents[SolverServiceT])(
  implicit ec:                        ExecutionContext
) extends RestController[SolverServiceT](cc) {

  private val logger = Logger(this.getClass)

  def solve(): Action[JsValue] = RestActionJsonBody { implicit request =>
    read[Problems] { problems =>
      service
        .solve(problems)
        .map(response => {
          response match {
            case Left(error) =>
              logger.error(error.toString)
              logger.error(s"Problems: ${Json.toJson(problems)}")
            case Right(value) if value.solutions.isEmpty =>
              logger.warn("Solver found 0 solutions!")
              logger.warn(s"Problems: ${Json.toJson(problems)}")
            case Right(value) =>
              logger.debug(s"Solutions: ${Json.toJson(value)}")
          }
          write[Solutions](response)
        })
    }
  }
}
