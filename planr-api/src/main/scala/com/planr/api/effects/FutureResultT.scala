package com.planr.api.effects

import com.planr.api.messages.Error
import cats.data.EitherT

object FutureResultT {
  def pure[T](t: T): FutureResultT[T] = EitherT(FutureResult.pure(t))

  def fromFR[T](f: FutureResult[T]): FutureResultT[T] = EitherT(f)

  def fromOption[T](opt: Option[T], ifNone: => Error): FutureResultT[T] =
    EitherT(FutureResult.fromOption(opt, ifNone))

  def raiseError[T](e: Error): FutureResultT[T] = EitherT(FutureResult.raiseError(e))
}
