package com.planr.api.effect

import com.planr.api.msg.Error

object Result {
  def pure[T](v: T): Result[T] = Right(v)

  def raiseError[T](e: Error): Result[T] = Left[Error, T](e)

  def fromOption[T](opt: Option[T], ifNone: => Error): Result[T] = opt match {
    case None    => Result.raiseError(ifNone)
    case Some(v) => Result.pure(v)
  }

  val unit: Result[Unit] = this.pure(())
}
