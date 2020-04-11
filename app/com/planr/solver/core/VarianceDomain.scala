package com.planr.solver.core

import com.google.ortools.constraintsolver.{IntVar, IntervalVar, SequenceVar}

case class VarianceDomain(
  opKey:    String,
  interval: IntervalVar,
  sequence: SequenceVar,
  resource: IntVar
)
