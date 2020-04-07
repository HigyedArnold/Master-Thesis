package com.planr.api.effect

import com.planr.api.msg.Error
import scala.concurrent.Future

object FutureResult {

  def pure[T](v: T): FutureResult[T] = Future.successful(Right(v))

  def raiseError[T](e: Error): FutureResult[T] = Future.successful(Result.raiseError(e))

  def fromOption[T](opt: Option[T], ifNone: => Error): FutureResult[T] =
    Future.successful(Result.fromOption(opt, ifNone))

  val unit: FutureResult[Unit] = Future.successful(Result.unit)
}
