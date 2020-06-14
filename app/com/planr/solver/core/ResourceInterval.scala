package com.planr.solver.core

import com.google.ortools.constraintsolver.IntervalVar

case class ResourceInterval(
  interval: IntervalVar,
  key:      String
)
