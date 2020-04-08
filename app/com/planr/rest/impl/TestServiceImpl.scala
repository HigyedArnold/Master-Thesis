package com.planr.rest.impl

import com.google.inject.Inject
import com.planr.api.async.TestService
import com.planr.api.effect.FutureResult

class TestServiceImpl @Inject() () extends TestService {
  override def get: FutureResult[String] = FutureResult.pure("OK!")
}
