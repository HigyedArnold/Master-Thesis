package com.planr.api.msg

case class SolutionOperation(
  key:      String,
  name:     String,
  duration: Long,
  resource: Resource,
  interval: Interval,
  day:      Interval
)

case class Solution(
  cost:       Double, // [0.0, 100.0]
  duration:   Long,
  interval:   Interval,
  day:        Interval,
  operations: Array[SolutionOperation]
)

case class Solutions(
  solutions: Array[Solution]
)
