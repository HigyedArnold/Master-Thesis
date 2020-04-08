package com.planr.api.messages

case class Error(
  scope:   String,
  code:    Int,
  message: String
) {
  override def toString: String = s"Error($scope, $code, $message)"
}
