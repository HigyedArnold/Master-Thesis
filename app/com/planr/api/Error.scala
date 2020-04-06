package com.planr.api

case class Error(
  scope:   String,
  code:    Int,
  message: String
)
