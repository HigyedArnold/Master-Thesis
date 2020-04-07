package com.planr.rest.impl

import com.google.inject.Inject
import com.planr.api.async.TestService
import com.planr.api.msg.Error

import scala.concurrent.Future

class TestServiceImpl @Inject()() extends TestService {
  override def get: Future[Either[Error, String]] = Future.successful(Right("OK!"))
}
