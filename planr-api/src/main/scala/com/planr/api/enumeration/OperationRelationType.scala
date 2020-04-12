package com.planr.api.enumeration

/**
  * Documentation: https://google.github.io/or-tools/cpp_routing/classoperations__research_1_1Solver.html
  */
object OperationRelationType extends Enumeration {
  type OperationRelationType = Value

  val ENDS_AFTER_END:     OperationRelationType.Value = Value("ENDS_AFTER_END")
  val ENDS_AFTER_START:   OperationRelationType.Value = Value("ENDS_AFTER_START")
  val ENDS_AT_END:        OperationRelationType.Value = Value("ENDS_AT_END")
  val ENDS_AT_START:      OperationRelationType.Value = Value("ENDS_AT_START")
  val STARTS_AFTER_END:   OperationRelationType.Value = Value("STARTS_AFTER_END")
  val STARTS_AFTER_START: OperationRelationType.Value = Value("STARTS_AFTER_START")
  val STARTS_AT_END:      OperationRelationType.Value = Value("STARTS_AT_END")
  val STARTS_AT_START:    OperationRelationType.Value = Value("STARTS_AT_START")
}
