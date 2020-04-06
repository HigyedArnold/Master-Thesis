package com.planr.api.enu

object RankType extends Enumeration {
  type RankType = Value

  val BEST: RankType.Value = Value("BEST")
  val GOOD: RankType.Value = Value("GOOD")
  val BAD:  RankType.Value = Value("BAD")
}
