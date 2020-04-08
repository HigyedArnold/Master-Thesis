package com.planr.api.messages

object ErrorCodes {
  /** Package */
  val REST__ERROR   = 1000
  val SOVLER__ERROR = 1100

  /** Specific */
  val JSON_SERIALIZATION__ERROR    = 1
  val JSON_EMPTY__ERROR            = 2
  val UNKNOWN_ACTOR_MESSAGE__ERROR = 3
}
