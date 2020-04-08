package com.planr.api

import com.planr.api.msg.Error
import cats.data.EitherT

import scala.concurrent.Future

package object effect {
  /** Either of Error and T */
  type Result[T] = Either[Error, T]
  /** Future of Either of Error and T */
  type FutureResult[T] = Future[Either[Error, T]]
  /** Monad transformer of Future of Either of Error and T */
  type FutureResultT[T] = EitherT[Future, Error, T]
}
