package com.planr.api

import com.planr.api

object RankType extends Enumeration {
  type RankType = Value

  val BEST: api.RankType.Value = Value("BEST")
  val GOOD: api.RankType.Value = Value("GOOD")
  val BAD:  api.RankType.Value = Value("BAD")
}
