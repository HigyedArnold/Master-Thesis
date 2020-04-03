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

/** By order */
case class Operations(
  preOperations:  Array[Operation],
  operations:     Array[Operation],
  postOperations: Array[Operation],
  supOperations:  Array[Operation]
)

case class TimeInterval(
  start: Long,
  stop:  Long
)

case class DateTimeInterval(
  start: Long,
  stop:  Long
)

case class Allocation(
  resourceKey: String,
  intervals:   Array[DateTimeInterval]
)

case class Resource(
  key:      String,
  name:     String,
  groupKey: String,
  resType:  ResourceType
)

case class Program(
  preOperation:  TimeInterval,
  operation:     TimeInterval,
  postOperation: TimeInterval,
  supOperation:  TimeInterval
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

case class InInterval(
  interval:      DateTimeInterval,
  operationKeys: Array[String]
)

case class Constraints(
  operationGrids: Option[Array[OperationGrid]], // SAME_TYPES
  sameResource:   Option[Array[SameResource]],  // SAME_TYPES
  inIntervals:    Option[Array[InInterval]]     // ALL_TYPES
)

case class Problem(
  key:         String, // Tracing data of the request
  operations:  Operations,
  allocations: Array[Allocation],
  resources:   Array[Resource],
  program:     Program,
  days:        Array[DateTimeInterval],
  costs:       Option[Costs], // With default
  constraints: Option[Constraints] // With default
)
