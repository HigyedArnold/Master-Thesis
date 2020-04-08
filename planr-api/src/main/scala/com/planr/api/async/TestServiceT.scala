package com.planr.api.async

import com.planr.api.effect.FutureResult

trait TestServiceT {
  def get: FutureResult[String]
}
