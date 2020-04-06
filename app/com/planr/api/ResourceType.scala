package com.planr.api

import com.planr.api

object ResourceType extends Enumeration {
  type ResourceType = Value

  val Person:    api.ResourceType.Value = Value("Person")
  val Machine:   api.ResourceType.Value = Value("Machine")
  val Robot:     api.ResourceType.Value = Value("Robot")
  val Undefined: api.ResourceType.Value = Value("Undefined")
}
