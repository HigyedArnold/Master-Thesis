package com.planr.api

import com.planr.api.OperationType.OperationType
import com.planr.api.RankType.RankType

case class SolutionOperation(
  key:      String,
  name:     String,
  opType:   OperationType,
  duration: Long,
  interval: Interval,
  resource: Resource
)

/** By order */
case class SolutionOperations(
  preOperations:  Array[SolutionOperation],
  operations:     Array[SolutionOperation],
  postOperations: Array[SolutionOperation],
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
