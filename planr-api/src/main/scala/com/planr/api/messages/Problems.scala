package com.planr.api.messages

import com.planr.api.enumeration.OperationRelationType.OperationRelationType

case class TimeInterval(
  startT: Long,
  stopT:  Long
)

case class DateTimeInterval(
  startDt: Long,
  stopDt:  Long
)

case class Operation(
  key:          String,
  name:         String,
  duration:     Long,
  resourceKeys: Array[String]
)

case class Resource(
  key:  String,
  name: String
)

case class Costs(
  asSoonAsPossible:      Option[Boolean],     // ASAP
  asTightAsPossible:     Option[Boolean],     // ATAP
  preferredTimeInterval: Option[TimeInterval] // PTI
)

case class OperationRelation(
  opRelType: OperationRelationType,
  opKey1:    String,
  opKey2:    String
)

case class Constraints(
  operationGrid:        Option[Long], // X % 60 divisible by value
  sameResource:         Option[Array[String]],
  enforcedTimeInterval: Option[TimeInterval],
  operationsRelation:   Option[Array[OperationRelation]]
)

case class Problem(
  key:         String, // Tracing data of the request
  operations:  Array[Operation],
  resources:   Array[Resource],
  costs:       Option[Costs],
  constraints: Option[Constraints]
)

case class Allocation(
  resourceKey: String,
  intervals:   Array[DateTimeInterval]
)

case class DayFrame(
  day:         DateTimeInterval,
  program:     TimeInterval,
  allocations: Array[Allocation]
)

case class Problems(
  problem:        Problem, // Problem always relative to dayFrame
  dayFrames:      Array[DayFrame],
  searchInterval: Option[Long]
)
