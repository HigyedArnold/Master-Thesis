package com.planr.api.messages

case class SolutionOperation(
  key:      String,
  name:     String,
  duration: Long,
  resource: Resource,
  interval: DateTimeInterval
)

case class Solution(
  cost:       Double, // [0.0, 100.0]
  duration:   Long,
  day:        DateTimeInterval,
  interval:   DateTimeInterval,
  operations: Array[SolutionOperation]
)

case class Solutions(
  solutions: Array[Solution]
)
