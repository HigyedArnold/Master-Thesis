package com.planr.api.effects

import com.planr.api.messages.Error

object Result {
  def pure[T](v: T): Result[T] = Right(v)

  def raiseError[T](e: Error): Result[T] = Left[Error, T](e)

  def fromOption[T](opt: Option[T], ifNone: => Error): Result[T] = opt match {
    case None    => raiseError(ifNone)
    case Some(v) => pure(v)
  }

  val unit: Result[Unit] = pure(())
}
