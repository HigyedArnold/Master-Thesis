package com.planr.solver.core

import com.google.ortools.constraintsolver._

case class SolverProblem(
  sequences: Array[SequenceVar],
  resources: Array[IntVar],
  mainPhase: DecisionBuilder,
  objective: OptimizeVar,
  collector: SolutionCollector,
  timeLimit: RegularLimit
)