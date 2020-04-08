package com.planr.rest.json

import com.planr.api.enumeration.OperationRelationType
import com.planr.api.messages._
import play.api.libs.json.{Format, Json}

object JsonSerializers {

  // Error
  implicit val ErrorFormat: Format[Error] = Json.format[Error]

  // Problems
//  implicit val OperationTypeFormat: Format[OperationType.Value] = Json.formatEnum(OperationType)
//  implicit val ResourceTypeFormat:  Format[ResourceType.Value]  = Json.formatEnum(ResourceType)
  implicit val OperationRelationTypeFormat: Format[OperationRelationType.Value] = Json.formatEnum(OperationRelationType)

  implicit val TimeIntervalFormat:      Format[TimeInterval]      = Json.format[TimeInterval]
  implicit val DateTimeIntervalFormat:  Format[DateTimeInterval]  = Json.format[DateTimeInterval]
  implicit val OperationFormat:         Format[Operation]         = Json.format[Operation]
  implicit val ResourceFormat:          Format[Resource]          = Json.format[Resource]
  implicit val CostsFormat:             Format[Costs]             = Json.format[Costs]
  implicit val OperationRelationFormat: Format[OperationRelation] = Json.format[OperationRelation]
  implicit val ConstraintsFormat:       Format[Constraints]       = Json.format[Constraints]
  implicit val ProblemFormat:           Format[Problem]           = Json.format[Problem]
  implicit val AllocationFormat:        Format[Allocation]        = Json.format[Allocation]
  implicit val DayFrameFormat:          Format[DayFrame]          = Json.format[DayFrame]
  implicit val ProblemsFormat:          Format[Problems]          = Json.format[Problems]

  // Solutions
//  implicit val RankTypeFormat: Format[RankType.Value] = Json.formatEnum(RankType)

  implicit val SolutionOperationFormat: Format[SolutionOperation] = Json.format[SolutionOperation]
  implicit val SolutionFormat:          Format[Solution]          = Json.format[Solution]
  implicit val SolutionsFormat:         Format[Solutions]         = Json.format[Solutions]

}
