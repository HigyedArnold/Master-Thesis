package com.planr.rest.controller

import com.google.inject.Inject
import com.planr.api.async.TestServiceT
import com.planr.rest.api.{RestController, RestControllerComponents}
import play.api.mvc.Action

import scala.concurrent.ExecutionContext

class TestController @Inject()(cc: RestControllerComponents[TestServiceT])(
  implicit ec:                      ExecutionContext
) extends RestController[TestServiceT](cc) {

  def get(): Action[Unit] = RestActionEmptyBody { _ =>
    service.get.map(write[String])
  }

}
