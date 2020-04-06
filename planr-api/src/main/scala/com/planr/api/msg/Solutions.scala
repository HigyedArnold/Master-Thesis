package com.planr.api.msg

import com.planr.api.enu.OperationType.OperationType
import com.planr.api.enu.RankType.RankType

case class SolutionOperation(
  key:      String,
  name:     String,
  opType:   OperationType,
  day:      Interval,
  duration: Long,
  interval: Interval,
  resource: Resource
)

case class SolutionOperations(
  preOperations:  Array[SolutionOperation], // By order
  operations:     Array[SolutionOperation], // By order
  postOperations: Array[SolutionOperation], // By order
  supOperations:  Array[SolutionOperation]
)

case class Solution(
  cost:          Double, // [0.0, 100.0]
  ranking:       RankType,
  totalDuration: Long,
  interval:      Interval,
  operations:    SolutionOperations
)

case class Solutions(
  solutions: Array[Solution]
)
