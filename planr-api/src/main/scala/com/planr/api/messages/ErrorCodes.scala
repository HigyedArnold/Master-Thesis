package com.planr.api.messages

object ErrorCodes {
  /** Package */
  val API__ERROR    = 1000
  val REST__ERROR   = 1100
  val SOVLER__ERROR = 1200

  /** Specific */
  val JSON_SERIALIZATION__ERROR    = 1
  val JSON_EMPTY__ERROR            = 2
  val UNKNOWN_ACTOR_MESSAGE__ERROR = 3
  val APP_CONFIG__ERROR            = 4
}
