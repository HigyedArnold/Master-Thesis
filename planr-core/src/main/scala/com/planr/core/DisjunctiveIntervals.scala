package com.planr.core

import com.google.ortools.constraintsolver._

import scala.annotation.tailrec

class DisjunctiveIntervals {

//  NativeLibLoader.init()

  case class Interval(start: Long, end: Long)

  // Test 1
  val blockedIntervals1: List[Interval] = List(
    Interval(7,  10),
    Interval(8,  9),
    Interval(10, 12)
  )

  val requestIntervals1: List[Long] = List(
    2,
    4
  )
//  search(0, 10, blockedIntervals1, requestIntervals1)

  /**
    *
    * @param start value
    * @param end value
    * @param blockedIntervals allocated intervals that are disjunctive with the solution intervals
    * @param requestIntervals requested intervals fixed duration
    */
  def search(start: Long, end: Long, blockedIntervals: List[Interval], requestIntervals: List[Long]): Unit = {

    val solver = new Solver("DisjunctiveIntervals")

    // INTERVALS
    val allIntervals = requestIntervals
      .map(duration => solver.makeFixedDurationIntervalVar(start, end - duration, duration, false, "interval_" + start + "..." + (end - duration) + "_" + duration))
      .toArray
    val allBlockedIntervals = mergeIntervals(blockedIntervals)
      .map(
        interval =>
          solver.makeFixedDurationIntervalVar(interval.start, interval.start, interval.end - interval.start, false, "blocked_interval_" + interval.start + "_" + interval.end)
      )
      .toArray

    // CONSTRAINTS
    // Priority constraint
    for (i <- 0 until requestIntervals.size - 1) {
      solver.addConstraint(solver.makeIntervalVarRelation(allIntervals(i + 1), Solver.STARTS_AFTER_END, allIntervals(i)))
    }

    // Disjunctive constraint
    solver.addConstraint(solver.makeDisjunctiveConstraint(allIntervals ++ allBlockedIntervals, "all_disjunctive_constraint"))
    // Trick to don't print blocked intervals
    val sequences = Array(solver.makeDisjunctiveConstraint(allIntervals, "disjunctive_constraint").makeSequenceVar())

    // OBJECTIVES
    val startVar     = allIntervals.head.startExpr()
    val endVar       = allIntervals.last.endExpr()
    val objectiveVar = solver.makeSum(startVar, solver.makeDifference(endVar, startVar)).`var`()

    val sequencePhase       = solver.makePhase(sequences,        Solver.SEQUENCE_DEFAULT)
    val objectiveStartPhase = solver.makePhase(startVar.`var`(), Solver.CHOOSE_FIRST_UNBOUND, Solver.ASSIGN_MIN_VALUE)
    val objectiveEndPhase   = solver.makePhase(endVar.`var`(),   Solver.CHOOSE_FIRST_UNBOUND, Solver.ASSIGN_MIN_VALUE)
    val mainPhase           = solver.compose(sequencePhase,      objectiveStartPhase, objectiveEndPhase)

    val searchLog = solver.makeSearchLog(1000000)
    val collector = solver.makeAllSolutionCollector()
    collector.add(sequences)
    collector.addObjective(objectiveVar)

    for (i <- sequences.indices) {
      val sequence = sequences(i)
      for (j <- 0 until sequence.size.toInt) {
        val t = sequence.interval(j)
        collector.add(t.startExpr().`var`())
        collector.add(t.endExpr().`var`())
      }
    }

    if (solver.solve(mainPhase, searchLog, collector)) {
      // STATISTICS
      println
      println("Solutions: " + solver.solutions)
      println("Failures: " + solver.failures)
      println("Branches: " + solver.branches)
      println("Wall time: " + solver.wallTime + "ms")
      println
      for (i <- 0 until collector.solutionCount) {
        println("Objective value: " + collector.objectiveValue(i))
        val seq = sequences(0)
        collector
          .forwardSequence(i, seq)
          .foreach(v => {
            val t = seq.interval(v)
            print("Interval: " + v + " (" + collector.value(i, t.startExpr().`var`()) + ", " + collector.value(i, t.endExpr().`var`()) + ") ")
          })
        println
      }
    }

    ()
  }

  private def mergeIntervals(blockedIntervals: List[Interval]): List[Interval] =
    if (blockedIntervals.isEmpty) blockedIntervals
    else {
      val intervals = blockedIntervals.sortWith((i1, i2) => i1.start - i2.start < 0)
      recMergeIntervals(intervals, List(intervals.head))
    }

  @tailrec
  private def recMergeIntervals(blockedIntervals: List[Interval], mergedIntervals: List[Interval]): List[Interval] = blockedIntervals match {
    case Nil                                          => mergedIntervals.reverse
    case h :: t if mergedIntervals.head.end < h.start => recMergeIntervals(t, h :: mergedIntervals)
    case h :: t                                       => recMergeIntervals(t, Interval(mergedIntervals.head.start, scala.math.max(mergedIntervals.head.end, h.end)) :: mergedIntervals.tail)
  }
}
