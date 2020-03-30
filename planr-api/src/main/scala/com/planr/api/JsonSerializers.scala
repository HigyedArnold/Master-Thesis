package com.planr.api

import play.api.libs.json.{Format, Json}

object JsonSerializers {

  implicit val operationTypeFormat: Format[OperationType.Value] = Json.formatEnum(OperationType)

  implicit val operationFormat: Format[Operation] = Json.format[Operation]
}
