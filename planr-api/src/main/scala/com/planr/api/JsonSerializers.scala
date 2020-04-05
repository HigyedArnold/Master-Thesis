package com.planr.api

import play.api.libs.json.{Format, Json}

object JsonSerializers {

  // Error
  implicit val ErrorFormat: Format[Error] = Json.format[Error]

  // Problem
  implicit val OperationTypeFormat: Format[OperationType.Value] = Json.formatEnum(OperationType)
  implicit val ResourceTypeFormat:  Format[ResourceType.Value]  = Json.formatEnum(ResourceType)

  implicit val OperationFormat:     Format[Operation]     = Json.format[Operation]
  implicit val OperationsFormat:    Format[Operations]    = Json.format[Operations]
  implicit val IntervalFormat:      Format[Interval]      = Json.format[Interval]
  implicit val AllocationFormat:    Format[Allocation]    = Json.format[Allocation]
  implicit val ResourceFormat:      Format[Resource]      = Json.format[Resource]
  implicit val ProgramFormat:       Format[Program]       = Json.format[Program]
  implicit val CostsFormat:         Format[Costs]         = Json.format[Costs]
  implicit val SameResourceFormat:  Format[SameResource]  = Json.format[SameResource]
  implicit val OperationGridFormat: Format[OperationGrid] = Json.format[OperationGrid]
  implicit val ConstraintsFormat:   Format[Constraints]   = Json.format[Constraints]
  implicit val ProblemFormat:       Format[Problems]       = Json.format[Problems]

  // Solution
  implicit val RankTypeFormat: Format[RankType.Value] = Json.formatEnum(RankType)

  implicit val SolutionOperationFormat:  Format[SolutionOperation]  = Json.format[SolutionOperation]
  implicit val SolutionOperationsFormat: Format[SolutionOperations] = Json.format[SolutionOperations]
  implicit val SolutionFormat:           Format[Solution]           = Json.format[Solution]
  implicit val SolutionsFormat:          Format[Solutions]          = Json.format[Solutions]

}
