package com.planr.api.async

import com.planr.api.effect.FutureResult

trait TestService {
  def get: FutureResult[String]
}
