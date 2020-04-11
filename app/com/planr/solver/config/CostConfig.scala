package com.planr.solver.config

object CostConfig {
  val PERCENTAGE: Long = 100
  val PRECISION:  Long = 100
  val MAX_COST:   Long = 100

  val ASAP_PERCENTAGE: Long = (50.00 * PRECISION).toLong // %
  val ATAP_PERCENTAGE: Long = (25.00 * PRECISION).toLong // %
  val PTI_PERCENTAGE:  Long = (25.00 * PRECISION).toLong // %
  // _______________________________+
  //                          100.00 %
}
