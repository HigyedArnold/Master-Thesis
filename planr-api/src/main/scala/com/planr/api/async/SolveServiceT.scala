package com.planr.api.async

import com.planr.api.effect.FutureResult
import com.planr.api.msg.{Problems, Solutions}

trait SolveServiceT {
  def solve(problems: Problems): FutureResult[Solutions]
}
