package com.planr.rest.api

import play.api.i18n.MessagesApi
import play.api.mvc.{MessagesRequestHeader, PreferredMessagesProvider, Request, WrappedRequest}

/**
  * A wrapped request for rest resources.
  *
  * This is commonly used to hold request-specific information like
  * security credentials, and useful shortcut methods.
  */
trait RestRequestHeader extends MessagesRequestHeader with PreferredMessagesProvider

class RestRequest[T](request: Request[T], val messagesApi: MessagesApi) extends WrappedRequest(request) with RestRequestHeader

object RestRequest {
  def apply[T](request: Request[T], messagesApi: MessagesApi): RestRequest[T] = new RestRequest[T](request, messagesApi)
}
