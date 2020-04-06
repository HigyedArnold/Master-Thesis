package com.planr.rest

import com.google.inject.Inject
import com.planr.api.Error

import scala.concurrent.Future

class MainServiceImpl @Inject() () extends MainService {
  override def index: Future[Either[Error, String]] = Future.successful(Right("RST server started..."))
}
