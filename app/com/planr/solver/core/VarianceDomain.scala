package com.planr.solver.core

import com.google.ortools.constraintsolver.{IntVar, IntervalVar}

case class VarianceDomain(
  opKey:    String,
  interval: IntervalVar,
  resource: IntVar
)
