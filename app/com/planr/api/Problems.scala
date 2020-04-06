package com.planr.api

import com.planr.api.OperationType.OperationType
import com.planr.api.ResourceType.ResourceType

case class Operation(
  key:          String,
  name:         String,
  opType:       OperationType,
  duration:     Option[Long],
  resourceKeys: Array[String]
)

case class Operations(
  preOperations:  Array[Operation], // By order
  operations:     Array[Operation], // By order
  postOperations: Array[Operation], // By order
  supOperations:  Array[Operation]
)

case class Interval(
  start: Long,
  stop:  Long
)

case class Allocation(
  resourceKey: String,
  intervals:   Array[Interval]
)

case class Resource(
  key:      String,
  name:     String,
  groupKey: String,
  resType:  ResourceType
)

case class Program(
  day:           Interval,
  preOperation:  Interval,
  operation:     Interval,
  postOperation: Interval,
  supOperation:  Interval,
  allocations:   Array[Allocation]
)

case class Costs(
  asSoonAsPossible:  Option[Boolean], // ASAP
  asTightAsPossible: Option[Boolean], // ATAP
  beforeTime:        Option[Long],    // BT
  afterTime:         Option[Long]     // AT
)

case class OperationGrid(
  opType: OperationType,
  value:  Long // X % 60 divisible by value
)

case class SameResource(
  opType:        OperationType,
  operationKeys: Array[String]
)

case class Constraints(
  operationGrids: Option[Array[OperationGrid]], // SAME_TYPES
  sameResource:   Option[Array[SameResource]]   // SAME_TYPES
)

case class Problems(
  key:         String, // Tracing data of the request
  operations:  Operations,
  resources:   Array[Resource],
  programs:    Array[Program],
  costs:       Option[Costs],
  constraints: Option[Constraints]
)
