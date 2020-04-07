package com.planr.rest.impl

import com.google.inject.Inject
import com.planr.api.async.TestService
import com.planr.api.effect.FutureResultT
import com.planr.api.effect.implicits._

class TestServiceImpl @Inject() () extends TestService {
  override def get: FutureResultT[String] = "OK!".asPureFRT
}
