package com.planr.api.msg

case class Error(
  scope:   String,
  code:    Int,
  message: String
)
