package com.planr.rest

import com.google.inject.Inject
import com.planr.rest.api.{RestController, RestControllerComponents}
import play.api.mvc.Action

import scala.concurrent.ExecutionContext

class MainController @Inject() (cc: RestControllerComponents[MainService])(
  implicit ec:                      ExecutionContext
) extends RestController[MainService](cc) {

  def index(): Action[Unit] = RestActionEmptyBody { implicit request =>
    service.index.map(write[String])
  }

}
