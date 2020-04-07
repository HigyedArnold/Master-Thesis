package com.planr.api.async

import com.planr.api.effect.FutureResultT

trait TestService {
  def get: FutureResultT[String]
}
