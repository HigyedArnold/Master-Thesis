package com.planr.api.async

import com.planr.api.effects.FutureResult
import com.planr.api.messages.{Problems, Solutions}

trait SolveServiceT {
  def solve(problems: Problems): FutureResult[Solutions]
}
