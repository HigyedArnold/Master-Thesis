package com.planr.api.msg

case class SolutionOperation(
                              key:      String,
                              name:     String,
                              duration: Long,
                              resource: Resource,
                              interval: DateTimeInterval,
                              day:      DateTimeInterval
)

case class Solution(
                     cost:       Double, // [0.0, 100.0]
                     duration:   Long,
                     interval:   DateTimeInterval,
                     day:        DateTimeInterval,
                     operations: Array[SolutionOperation]
)

case class Solutions(
  solutions: Array[Solution]
)
