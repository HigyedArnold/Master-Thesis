package com.planr.api

final case class Error(scope: String, code: Int, message: String) {
  override def toString: String = s"Error($scope, $code, $message)"
}
