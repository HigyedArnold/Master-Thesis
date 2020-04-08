package com.planr.rest.impl

import com.google.inject.Inject
import com.planr.api.async.TestServiceT
import com.planr.api.effect.FutureResult

class TestService @Inject() () extends TestServiceT {
  override def get: FutureResult[String] = FutureResult.pure("OK!")
}
