package com.planr.rest

import com.google.inject.Inject
import com.planr.api

import scala.concurrent.Future

class MainServiceImpl @Inject() () extends MainService {
  override def index: Future[Either[api.Error, String]] = Future.successful(Right("RST server started..."))
}
