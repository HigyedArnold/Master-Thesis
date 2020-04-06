package com.planr.api.async

import com.planr.api.msg.Error

import scala.concurrent.Future

trait MainService {
  def index: Future[Either[Error, String]]
}
