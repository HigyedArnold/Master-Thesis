package com.planr.api.effects

import com.planr.api.messages.Error

import scala.concurrent.Future

object FutureResultTSyntax {

  trait Implicits {

    implicit def convertFEToFutureResultOps[T](f: => FutureResult[T]): ConvertFEToFutureResultOps[T] =
      new ConvertFEToFutureResultOps[T](f)

    implicit def convertOptionToFutureResultOps[T](f: Option[T]): ConvertOptionToFutureResultOps[T] =
      new ConvertOptionToFutureResultOps(f)

    implicit def convertPureToFutureResultOps[T](f: T): ConvertPureToFutureResultOps[T] =
      new ConvertPureToFutureResultOps(f)

    implicit def convertErrorToFutureResultOps(f: Error): ConvertErrorToFutureResultOps =
      new ConvertErrorToFutureResultOps(f)
  }

  final class ConvertFEToFutureResultOps[T](f: => Future[Either[Error, T]]) {
    def asFRT: FutureResultT[T] = FutureResultT.fromFR(f)
  }

  final class ConvertOptionToFutureResultOps[T](o: Option[T]) {
    def asFRT(ifNone: => Error): FutureResultT[T] = FutureResultT.fromOption(o, ifNone)
  }

  final class ConvertPureToFutureResultOps[T](t: T) {
    def asPureFRT: FutureResultT[T] = FutureResultT.pure(t)
  }

  final class ConvertErrorToFutureResultOps(err: Error) {
    def asFailedFRT[T]: FutureResultT[T] = FutureResultT.raiseError[T](err)
  }

}
