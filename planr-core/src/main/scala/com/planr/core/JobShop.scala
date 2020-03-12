package com.planr.core

import com.google.ortools.constraintsolver._
import com.planr.core.util.NativeLibLoader

import scala.collection.mutable.ArrayBuffer

/**
  * https://developers.google.com/optimization/scheduling/job_shop in Scala
  */
object JobShop extends App {

  NativeLibLoader.init()

  final case class Task(machineID: Int, duration: Int)

  // Test 1
  val jobs1 = List(
    List(Task(0, 3), Task(1, 2), Task(2, 2)), // Job0
    List(Task(0, 2), Task(2, 1), Task(1, 4)), // Job1
    List(Task(1, 4), Task(2, 3)) // Job2
  )
  search(jobs1)

  // Test 2
  // abz9
  // http://web.cecs.pdx.edu/~bart/cs510ss/project/jobshop/jobshop/abz9.txt
  val jobs2 = List(
    List(Task(6, 14), Task(5, 21), Task(8, 13), Task(4, 11), Task(1, 11), Task(14, 35), Task(13, 20), Task(11, 17), Task(10, 18), Task(12, 11), Task(2, 23), Task(3, 13), Task(0, 15), Task(7, 11), Task(9, 35)), // Job0
    List(Task(1, 35), Task(5, 31), Task(0, 13), Task(3, 26), Task(6, 14), Task(9, 17), Task(7, 38), Task(12, 20), Task(10, 19), Task(13, 12), Task(8, 16), Task(4, 34), Task(11, 15), Task(14, 12), Task(2, 14)), // Job1
    List(Task(0, 30), Task(4, 35), Task(2, 40), Task(10, 35), Task(6, 30), Task(14, 23), Task(8, 29), Task(13, 37), Task(7, 38), Task(3, 40), Task(9, 26), Task(12, 11), Task(1, 40), Task(11, 36), Task(5, 17)), // Job2
    List(Task(7, 40), Task(5, 18), Task(4, 12), Task(8, 23), Task(0, 23), Task(9, 14), Task(13, 16), Task(12, 14), Task(10, 23), Task(3, 12), Task(6, 16), Task(14, 32), Task(1, 40), Task(11, 25), Task(2, 29)), // Job3
    List(Task(2, 35), Task(3, 15), Task(12, 31), Task(11, 28), Task(6, 32), Task(4, 30), Task(10, 27), Task(7, 29), Task(0, 38), Task(13, 11), Task(1, 23), Task(14, 17), Task(5, 27), Task(9, 37), Task(8, 29)), // Job4
    List(Task(5, 33), Task(3, 33), Task(6, 19), Task(12, 40), Task(10, 19), Task(0, 33), Task(13, 26), Task(2, 31), Task(11, 28), Task(7, 36), Task(4, 38), Task(1, 21), Task(14, 25), Task(9, 40), Task(8, 35)), // Job5
    List(Task(13, 25), Task(0, 32), Task(11, 33), Task(12, 18), Task(4, 32), Task(6, 28), Task(5, 15), Task(3, 35), Task(9, 14), Task(2, 34), Task(7, 23), Task(10, 32), Task(1, 17), Task(14, 26), Task(8, 19)), // Job6
    List(Task(2, 16), Task(12, 33), Task(9, 34), Task(11, 30), Task(13, 40), Task(8, 12), Task(14, 26), Task(5, 26), Task(6, 15), Task(3, 21), Task(1, 40), Task(4, 32), Task(0, 14), Task(7, 30), Task(10, 35)), // Job7
    List(Task(2, 17), Task(10, 16), Task(14, 20), Task(6, 24), Task(8, 26), Task(3, 36), Task(12, 22), Task(0, 14), Task(13, 11), Task(9, 20), Task(7, 23), Task(1, 29), Task(11, 23), Task(4, 15), Task(5, 40)), // Job8
    List(Task(4, 27), Task(9, 37), Task(3, 40), Task(11, 14), Task(13, 25), Task(7, 30), Task(0, 34), Task(2, 11), Task(5, 15), Task(12, 32), Task(1, 36), Task(10, 12), Task(14, 28), Task(8, 31), Task(6, 23)), // Job9
    List(Task(13, 25), Task(0, 22), Task(3, 27), Task(8, 14), Task(5, 25), Task(6, 20),Task(14, 18), Task(7, 14), Task(1, 19), Task(2, 17), Task(4, 27), Task(9, 22), Task(12, 22), Task(11, 27), Task(10, 21)), // Job10
    List(Task(14, 34), Task(10, 15), Task(0, 22), Task(3, 29), Task(13, 34), Task(6, 40), Task(7, 17), Task(2, 32), Task(12, 20), Task(5, 39), Task(4, 31), Task(11, 16), Task(1, 37), Task(8, 33), Task(9, 13)), // Job11
    List(Task(6, 12), Task(12, 27), Task(4, 17), Task(2, 24), Task(8, 11), Task(5, 19), Task(14, 11), Task(3, 17), Task(9, 25), Task(1, 11), Task(11, 31), Task(13, 33), Task(7, 31), Task(10, 12), Task(0, 22)), // Job12
    List(Task(5, 22), Task(14, 15), Task(0, 16), Task(8, 32), Task(7, 20), Task(4, 22), Task(9, 11), Task(13, 19), Task(1, 30), Task(12, 33), Task(6, 29), Task(11, 18), Task(3, 34), Task(10, 32), Task(2, 18)), // Job13
    List(Task(5, 27), Task(3, 26), Task(10, 28), Task(6, 37), Task(4, 18), Task(12, 12), Task(11, 11), Task(13, 26), Task(7, 27), Task(9, 40), Task(14, 19), Task(1, 24), Task(2, 18), Task(0, 12), Task(8, 34)), // Job14
    List(Task(8, 15), Task(5, 28), Task(9, 25), Task(6, 32), Task(1, 13), Task(7, 38), Task(11, 11), Task(2, 34), Task(4, 25), Task(0, 20), Task(10, 32), Task(3, 23), Task(12, 14), Task(14, 16), Task(13, 20)), // Job15
    List(Task(1, 15), Task(4, 13), Task(8, 37), Task(3, 14), Task(10, 22), Task(5, 24), Task(12, 26), Task(7, 22), Task(9, 34), Task(14, 22), Task(11, 19), Task(13, 32), Task(0, 29), Task(2, 13), Task(6, 35)), // Job16
    List(Task(7, 36), Task(5, 33), Task(13, 28), Task(9, 20), Task(10, 30), Task(4, 33), Task(14, 29), Task(0, 34), Task(3, 22), Task(11, 12), Task(6, 30), Task(8, 12), Task(1, 35), Task(2, 13), Task(12, 35)), // Job17
    List(Task(14, 26), Task(11, 31), Task(5, 35), Task(2, 38), Task(13, 19), Task(10, 35), Task(4, 27), Task(8, 29), Task(3, 39), Task(9, 13), Task(6, 14), Task(7, 26), Task(0, 17), Task(1, 22), Task(12, 15)), // Job18
    List(Task(1, 36), Task(7, 34), Task(11, 33), Task(8, 17), Task(14, 38), Task(6, 39), Task(5, 16), Task(3, 27), Task(13, 29), Task(2, 16), Task(0, 16), Task(4, 19), Task(9, 40), Task(12, 35), Task(10, 39)), // Job29
  )
//  search(jobs2)

  def search(jobs: List[List[Task]]): Unit = {
    val machinesCount = jobs.flatten.maxBy(_.machineID).machineID + 1
    val jobsCount     = jobs.size
    val horizon       = jobs.flatten.map(_.duration).sum

    val jobsToTasksBuffer     = (0 until jobsCount).toArray.map(_ => ArrayBuffer.empty[IntervalVar])
    val machinesToTasksBuffer = (0 until machinesCount).toArray.map(_ => ArrayBuffer.empty[IntervalVar])
    val allSequencesBuffer    = ArrayBuffer.empty[SequenceVar]

    val solver = new Solver("JobShop")

    for (jobID <- 0 until jobsCount)
      for (taskID <- jobs(jobID).indices) {
        val task        = jobs(jobID)(taskID)
        val intervalVar = solver.makeFixedDurationIntervalVar(0, horizon.toLong, task.duration.toLong, false, "interval_" + jobID + "_" + taskID)
        jobsToTasksBuffer(jobID).append(intervalVar)
        machinesToTasksBuffer(task.machineID).append(intervalVar)
      }

    val jobsToTasks     = jobsToTasksBuffer.map(_.toArray)
    val machinesToTasks = machinesToTasksBuffer.map(_.toArray)

    for (jobID <- 0 until jobsCount)
      for (taskID <- 0 until jobs(jobID).size - 1)
        solver.addConstraint(solver.makeIntervalVarRelation(jobsToTasks(jobID)(taskID + 1), Solver.STARTS_AFTER_END, jobsToTasks(jobID)(taskID)))

    for (machineID <- 0 until machinesCount) {
      val constraint = solver.makeDisjunctiveConstraint(machinesToTasks(machineID), "machine_" + machineID)
      solver.addConstraint(constraint)
      allSequencesBuffer.append(constraint.makeSequenceVar())
    }

    val allEnds = for {
      jobID <- (0 until jobsCount).toArray
    } yield jobsToTasks(jobID)(jobsToTasks(jobID).length - 1).endExpr().`var`()

    val allSequences     = allSequencesBuffer.toArray
    val objectiveVar     = solver.makeMax(allEnds).`var`()
    val objectiveMonitor = solver.makeMinimize(objectiveVar, 1)

    val sequencePhase  = solver.makePhase(allSequences, Solver.SEQUENCE_DEFAULT)
    val objectivePhase = solver.makePhase(objectiveVar, Solver.CHOOSE_FIRST_UNBOUND, Solver.ASSIGN_MIN_VALUE)

    // Local search
//    val firstSolution = solver.makeAssignment()
//    firstSolution.add(allSequences)
//    firstSolution.addObjective(objectiveVar)
//
//    val storeFirstSolution = solver.makeStoreAssignment(firstSolution)
//
//    val firstSolutionPhase = solver.compose(sequencePhase, objectivePhase, storeFirstSolution)
//
//    val firstSolutionFound = solver.solve(firstSolutionPhase)
//
//    if (firstSolutionFound)
//      println("First solution found: " + firstSolution.objectiveValue())
//    else
//      println("First solution not found!")
//    Define the LocalSearchOperators and LocalSearchFilters
//    ...
    // Local search

    val mainPhase = solver.compose(sequencePhase, objectivePhase)

    val searchLog = solver.makeSearchLog(1000000, objectiveMonitor)
    val collector = solver.makeLastSolutionCollector
    collector.add(allSequences)
    collector.addObjective(objectiveVar)

    for (seq <- allSequences.indices) {
      val sequence = allSequences(seq)
      for (i <- 0 until sequence.size.toInt) {
        val t = sequence.interval(i)
        collector.add(t.startExpr().`var`())
        collector.add(t.endExpr().`var`())
      }
    }

    if (solver.solve(mainPhase, searchLog, objectiveMonitor, collector)) {
      println
      println("Solutions: " + solver.solutions)
      println("Failures: " + solver.failures)
      println("Branches: " + solver.branches)
      println("Wall time: " + solver.wallTime + "ms")
      println
      println("Objective value: " + collector.objectiveValue(0))
      for (m <- 0 until machinesCount) {
        val seq = allSequences(m)
        print(seq.name + ": ")
        collector
          .forwardSequence(0, seq)
          .foreach(v => {
            val t = seq.interval(v)
            print("Job: " + v + " (" + collector.value(0, t.startExpr().`var`()) + ", " + collector.value(0, t.endExpr().`var`()) + ") ")
          })
        println
      }
    }

    ()
  }

}
