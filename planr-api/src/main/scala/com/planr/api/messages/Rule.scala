package com.planr.api.messages

import com.planr.api.effects.Result

class Rule(fail: () => Result[Unit]) {
  def apply(): Result[Unit] = fail()
}
