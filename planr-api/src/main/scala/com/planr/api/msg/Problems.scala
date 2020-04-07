package com.planr.api.msg

import com.planr.api.enu.OperationRelationType.OperationRelationType

case class Interval(
  startDt: Long,
  stopDt:  Long
)

case class TimeInterval(
  startT: Long,
  stopT:  Long
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

case class Allocation(
  resourceKey: String,
  intervals:   Array[TimeInterval]
)

case class Program(
  day:         Interval,
  program:     TimeInterval,
  allocations: Array[Allocation]
)

case class Costs(
  asSoonAsPossible:  Option[Boolean],     // ASAP
  asTightAsPossible: Option[Boolean],     // ATAP
  preferredInterval: Option[TimeInterval] // PT
)

case class OperationRelation(
  opRelType: OperationRelationType,
  opKey1:    String,
  opKey2:    String
)

case class Constraints(
  operationGrid:      Option[Long], // X % 60 divisible by value
  sameResource:       Option[Array[String]],
  operationsRelation: Option[Array[OperationRelation]]
)

case class Problems(
  key:         String, // Tracing data of the request
  operations:  Array[Operation],
  resources:   Array[Resource],
  programs:    Array[Program],
  costs:       Option[Costs],
  constraints: Option[Constraints]
)
