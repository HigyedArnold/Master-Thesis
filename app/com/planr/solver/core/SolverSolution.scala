package com.planr.solver.core

import com.google.ortools.constraintsolver.{IntVar, OptimizeVar, SequenceVar, SolutionCollector}

case class SolverSolution(
  sequences: Array[SequenceVar],
  resources: Array[IntVar],
  objective: OptimizeVar,
  collector: SolutionCollector
)
