package com.planr.rest.api

import javax.inject.Inject
import play.api.Logger
import play.api.http.HttpVerbs
import play.api.i18n.MessagesApi
import play.api.libs.json.JsValue
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

/**
  * The action builder for the rest resource.
  *
  * This is the place to put logging, metrics, to augment
  * the request with contextual data, and manipulate the
  * result.
  * @param messagesApi - used for translation
  * @param executionContext - execution context
  * @tparam C - request content type (for example Unit - no body, JsValue - body with json)
  */
abstract class RestActionBuilder[C] @Inject() (messagesApi: MessagesApi)(implicit val executionContext: ExecutionContext) extends ActionBuilder[RestRequest, C] with HttpVerbs {

  type RestRequestBlock[T] = RestRequest[T] => Future[Result]

  private val logger = Logger(this.getClass)

  override def invokeBlock[T](request: Request[T], block: RestRequestBlock[T]): Future[Result] = {
    logger.debug(s"RestAction invoked - method: ${request.method}, path: ${request.path} : query: ${request.rawQueryString}, body: ${request.body.toString}")
    val future = block(RestRequest(request, messagesApi))
    future.map { result =>
      request.method match {
        case GET | HEAD =>
          result.withHeaders("Cache-Control" -> s"max-age: 100")
        case _ =>
          result
      }
    }
  }
}

/**
  * Action to be used when request body is empty
  * @param messagesApi - used for translation
  * @param bodyParsers - body parsers
  */
case class RestActionWithEmptyBodyBuilder @Inject() (messagesApi: MessagesApi, bodyParsers: PlayBodyParsers)(implicit executionContext: ExecutionContext)
    extends RestActionBuilder[Unit](messagesApi) {
  override val parser: BodyParser[Unit] = bodyParsers.empty
}

/**
  * Action to be used when request body should be json
  * @param messagesApi - used for translation
  * @param bodyParsers - body parsers
  */
case class RestActionWithJsonBodyBuilder @Inject() (messagesApi: MessagesApi, bodyParsers: PlayBodyParsers)(implicit executionContext: ExecutionContext)
    extends RestActionBuilder[JsValue](messagesApi) {
  override val parser: BodyParser[JsValue] = bodyParsers.json
}
