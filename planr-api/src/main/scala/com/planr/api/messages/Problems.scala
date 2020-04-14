package com.planr.api.messages

import com.planr.api.effects.Result
import com.planr.api.enumeration.OperationRelationType.OperationRelationType
import com.planr.api.messages.ErrorCodes._

case class TimeInterval(
  startT: Long,
  stopT:  Long
)

case class DateTimeInterval(
  startDt: Long,
  stopDt:  Long
)

case class Operation(
  key:          String,
  name:         String,
  duration:     Long,
  resourceKeys: Array[String]
)

case class Resource(
  key:  String,
  name: String
)

case class Costs(
  asSoonAsPossible:      Option[Boolean],     // ASAP
  asTightAsPossible:     Option[Boolean],     // ATAP
  preferredTimeInterval: Option[TimeInterval] // PTI
)

case class OperationRelation(
  opRelType: OperationRelationType,
  opKey1:    String,
  opKey2:    String
)

case class Constraints(
  operationGrid:        Option[Long], // startExpr() and 60 divisible by operationGrid
  sameResource:         Option[Array[Array[String]]],
  enforcedTimeInterval: Option[TimeInterval],
  operationsRelation:   Option[Array[OperationRelation]]
)

case class Problem(
  key:         String, // Tracing data of the request
  operations:  Array[Operation],
  resources:   Array[Resource],
  costs:       Option[Costs],
  constraints: Option[Constraints]
)

case class Allocation(
  resourceKey: String,
  intervals:   Array[DateTimeInterval]
)

case class DayFrame(
  day:         DateTimeInterval,
  program:     TimeInterval,
  allocations: Array[Allocation]
)

case class Problems(
  problem:        Problem, // Problem relative to each dayFrame
  dayFrames:      Array[DayFrame],
  searchInterval: Option[Long] // ASAP cost relative to searchInterval
) {

  val HOUR: Long = 60L   // In minutes
  val DAY:  Long = 1440L // In minutes

  private val rules = List(
    /** Constraints */
    new Rule(() => {
      problem.constraints
        .flatMap(_.operationGrid)
        .fold(Result.unit)(operationGrid =>
          if (HOUR % operationGrid == 0L) Result.unit
          else Result.raiseError[Unit](Error(this.getClass.getName, API__ERROR + VALIDATION__ERROR, "Field 'operationGrid' must be positive divisor of 60!"))
        )
    }),
    new Rule(() => {
      problem.constraints
        .flatMap(_.sameResource)
        .fold(Result.unit)(sameResource => {
          val flatSameResource = sameResource.flatten
          if (!sameResource.forall(_.length >= 2))
            Result.raiseError[Unit](Error(this.getClass.getName, API__ERROR + VALIDATION__ERROR, "Field 'sameResource' must have 2 or more operation keys in each array!"))
          else if (flatSameResource.distinct.length != flatSameResource.length)
            Result.raiseError[Unit](Error(this.getClass.getName, API__ERROR + VALIDATION__ERROR, "Field 'sameResource' must have distinct arrays of operation keys!"))
          else if (!flatSameResource.toSet.subsetOf(problem.operations.map(_.key).toSet))
            Result
              .raiseError[Unit](Error(this.getClass.getName, API__ERROR + VALIDATION__ERROR, "Field 'sameResource' flattened must be subset or equal set of operation keys set!"))
          else
            Result.unit
        })
    }),
    new Rule(() => {
      problem.constraints
        .flatMap(_.enforcedTimeInterval)
        .fold(Result.unit)(enforcedTimeInterval =>
          if (enforcedTimeInterval.startT > 0L && enforcedTimeInterval.stopT < DAY && enforcedTimeInterval.startT < enforcedTimeInterval.stopT) Result.unit
          else
            Result
              .raiseError[Unit](Error(this.getClass.getName, API__ERROR + VALIDATION__ERROR, "Field 'enforcedTimeInterval' must be a valid TimeInterval!"))
        )
    }),
    new Rule(() => {
      problem.constraints
        .flatMap(_.operationsRelation)
        .fold(Result.unit)(operationsRelation =>
          if (operationsRelation.flatMap(opRel => Array(opRel.opKey1, opRel.opKey2)).toSet.subsetOf(problem.operations.map(_.key).toSet)) Result.unit
          else
            Result
              .raiseError[Unit](
                Error(this.getClass.getName, API__ERROR + VALIDATION__ERROR, "Field 'operationsRelation' flattened must be subset or equal set of operation keys set!")
              )
        )
    }),
    /** Costs */
    new Rule(() => {
      problem.costs
        .flatMap(_.preferredTimeInterval)
        .fold(Result.unit)(preferredTimeInterval =>
          if (preferredTimeInterval.startT > 0L && preferredTimeInterval.stopT < DAY && preferredTimeInterval.startT < preferredTimeInterval.stopT) Result.unit
          else
            Result
              .raiseError[Unit](Error(this.getClass.getName, API__ERROR + VALIDATION__ERROR, "Field 'preferredTimeInterval' must be a valid TimeInterval!"))
        )
    }),
    /** Resources */
    new Rule(
      () =>
        if (problem.operations.flatMap(_.resourceKeys).toSet.subsetOf(problem.resources.map(_.key).toSet)) Result.unit
        else
          Result
            .raiseError[Unit](Error(this.getClass.getName, API__ERROR + VALIDATION__ERROR, "All resources must be defined for operations resourceKeys!"))
    )
  )

  def validate: Result[Unit] =
    rules.foldLeft(Result.unit)((acc, rule) =>
      if (acc.isRight) rule()
      else acc
    )

}
