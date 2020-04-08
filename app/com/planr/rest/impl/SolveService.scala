package com.planr.rest.impl

import com.google.inject.Inject
import com.planr.api.async.SolveServiceT
import com.planr.api.effects.FutureResult
import com.planr.api.messages.{Problems, Solutions}

class SolveService @Inject() () extends SolveServiceT {
  override def solve(problems: Problems): FutureResult[Solutions] = ???
}
