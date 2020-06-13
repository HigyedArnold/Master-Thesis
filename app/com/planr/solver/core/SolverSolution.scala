package com.planr.solver.core

import com.google.ortools.constraintsolver.{IntVar, IntervalVar, OptimizeVar, SolutionCollector}

case class SolverSolution(
  intervals: Array[IntervalVar],
  resources: Array[IntVar],
  objective: OptimizeVar,
  collector: SolutionCollector
)
