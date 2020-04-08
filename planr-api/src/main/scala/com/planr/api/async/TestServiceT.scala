package com.planr.api.async

import com.planr.api.effects.FutureResult

trait TestServiceT {
  def get: FutureResult[String]
}
