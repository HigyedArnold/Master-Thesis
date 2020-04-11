package com.planr.api

import cats.data.EitherT
import com.planr.api.messages.Error

import scala.concurrent.Future

package object effects {
  /** Either of Error and T */
  type Result[T] = Either[Error, T]
  /** Future of Either of Error and T */
  type FutureResult[T] = Future[Either[Error, T]]
  /** Monad transformer of Future of Either of Error and T */
  type FutureResultT[T] = EitherT[Future, Error, T]
}
