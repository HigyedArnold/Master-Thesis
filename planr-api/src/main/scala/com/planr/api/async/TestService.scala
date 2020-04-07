package com.planr.api.async

import com.planr.api.msg.Error

import scala.concurrent.Future

trait TestService {
  def get: Future[Either[Error, String]]
}
