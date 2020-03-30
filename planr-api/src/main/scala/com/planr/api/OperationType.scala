package com.planr.api

import com.planr.api

/**
  * Use Enumeration since it's easier to serialize with Play 2 (play-json_X.xx)
  *
  * Case object vs Enumeration: https://stackoverflow.com/questions/1898932/case-objects-vs-enumerations-in-scala
  */
//sealed trait OperationType
//case object PreOperation  extends OperationType
//case object Operation     extends OperationType
//case object PostOperation extends OperationType
//case object SupOperation  extends OperationType

object OperationType extends Enumeration {
  type OperationType = Value

  val PreOperation:  api.OperationType.Value = Value("PreOperation")
  val Operation:     api.OperationType.Value = Value("Operation")
  val PostOperation: api.OperationType.Value = Value("PostOperation")
  val SupOperation:  api.OperationType.Value = Value("SupOperation")
}
