package com.planr.rest

import scala.concurrent.Future
import com.planr.api.Error

trait MainService {
  def index: Future[Either[Error, String]]
}
