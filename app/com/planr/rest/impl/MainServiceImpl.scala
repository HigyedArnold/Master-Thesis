package com.planr.rest.impl

import com.google.inject.Inject
import com.planr.api.async.MainService
import com.planr.api.msg.Error

import scala.concurrent.Future

class MainServiceImpl @Inject() () extends MainService {
  override def index: Future[Either[Error, String]] = Future.successful(Right("RST server started..."))
}
