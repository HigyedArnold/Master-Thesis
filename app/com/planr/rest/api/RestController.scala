package com.planr.rest.api

import com.planr.api.msg.ErrorCodes._
import com.planr.api.msg.Error
import com.planr.rest.json.JsonSerializers._
import javax.inject.Inject
import play.api.Logger
import play.api.http.FileMimeTypes
import play.api.i18n.{Langs, MessagesApi}
import play.api.libs.json._
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

/**
  * Packages up the component dependencies for the post controller.
  *
  * This is a good way to minimize the surface area exposed to the controller, so the
  * controller only has to have one thing injected.
  */
case class RestControllerComponents[S] @Inject() (
  restActionBuilder:     RestActionWithEmptyBodyBuilder,
  jsonRestActionBuilder: RestActionWithJsonBodyBuilder,
  service:               S,
  actionBuilder:         DefaultActionBuilder,
  parsers:               PlayBodyParsers,
  messagesApi:           MessagesApi,
  langs:                 Langs,
  fileMimeTypes:         FileMimeTypes,
  executionContext:      scala.concurrent.ExecutionContext
) extends ControllerComponents

/**
  * Exposes actions to the RestController by wiring the injected state into the base class.
  */
class RestController[S] @Inject() (cc: RestControllerComponents[S]) extends InjectedController {
  private val logger = Logger(this.getClass)

  def RestActionEmptyBody(block: RestRequest[Unit] => Future[Result]):    Action[Unit]    = cc.restActionBuilder.async(block)
  def RestActionJsonBody(block:  RestRequest[JsValue] => Future[Result]): Action[JsValue] = cc.jsonRestActionBuilder.async(block)

  def service: S = cc.service

  def read[T](f: T => Future[Result])(implicit request: RestRequest[JsValue], reads: Reads[T]): Future[Result] = {
    implicit val ec: ExecutionContext = cc.executionContext
    request.body
      .validate[T]
      .fold(
        errors =>
          Future {
            val err = Error(this.getClass.getName, REST__ERROR + JSON_SERIALIZATION__ERROR, s"Request json could not be serialized: ${JsError.toJson(errors).toString()}")
            logger.error(err.toString)
            BadRequest(Json.toJson(err))
          },
        resource => f(resource)
      )
  }

  def write[T: Manifest](response: Either[Error, T])(implicit writes: Writes[T]): Result = {
    response match {
      case Right(value: T) => Ok(Json.toJson(value))
      case Left(error) => writeError(error)
      case value =>
        val err = Error(this.getClass.getName, REST__ERROR + JSON_SERIALIZATION__ERROR, s"Response json could not be serialized: ${value.toString})")
        logger.error(err.toString)
        InternalServerError(Json.toJson(err))
    }
  }

  def write(response: Either[Error, Unit]): Result = {
    response match {
      case Right(_)    => Ok
      case Left(error) => writeError(error)
    }
  }

  def writeError(error: Error): Result = {
    logger.error(s"${Json.toJson(error)}")
    errorMapping.getOrElse(error.code, error.code) match {
      case BAD_REQUEST  => BadRequest(Json.toJson(error))
      case UNAUTHORIZED => Unauthorized(Json.toJson(error))
      case FORBIDDEN    => Forbidden(Json.toJson(error))
      case NOT_FOUND    => NotFound(Json.toJson(error))
      case _            => InternalServerError(Json.toJson(error))
    }
  }

  def errorMapping: Map[Int, Int] = Map.empty[Int, Int]

}
