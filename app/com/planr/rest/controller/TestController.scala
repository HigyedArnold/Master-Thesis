package com.planr.rest.controller

import com.google.inject.Inject
import com.planr.api.async.TestService
import com.planr.rest.api.{RestController, RestControllerComponents}
import play.api.mvc.Action

import scala.concurrent.ExecutionContext

class TestController @Inject() (cc: RestControllerComponents[TestService])(
  implicit ec:                      ExecutionContext
) extends RestController[TestService](cc) {

  def get(): Action[Unit] = RestActionEmptyBody { _ =>
    service.get.map(write[String])
  }

}
