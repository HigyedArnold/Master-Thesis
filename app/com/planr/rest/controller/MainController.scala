package com.planr.rest.controller

import com.google.inject.Inject
import com.planr.api.async.MainService
import com.planr.rest.api.{RestController, RestControllerComponents}
import play.api.mvc.Action

import scala.concurrent.ExecutionContext

class MainController @Inject() (cc: RestControllerComponents[MainService])(
  implicit ec:                      ExecutionContext
) extends RestController[MainService](cc) {

  def index(): Action[Unit] = RestActionEmptyBody { _ =>
    service.index.map(write[String])
  }

}
