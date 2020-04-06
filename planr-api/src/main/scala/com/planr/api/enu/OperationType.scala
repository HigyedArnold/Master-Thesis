package com.planr.api.enu

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
  /**
    * PreOperation < Operation < PostOperation
    * [              SupOperation            ]
    *
    * Observation: No parallel operations supported with except of SupOperation type!
    */
  type OperationType = Value

  val PreOperation:  OperationType.Value = Value("PreOperation")
  val Operation:     OperationType.Value = Value("Operation")
  val PostOperation: OperationType.Value = Value("PostOperation")
  val SupOperation:  OperationType.Value = Value("SupOperation")
}
