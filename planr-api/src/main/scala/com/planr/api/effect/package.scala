package com.planr.api

import com.planr.api.msg.Error
import cats.data.EitherT

import scala.concurrent.Future

package object effect {
  type Result[+T]       = Either[Error, T]
  type FutureResult[+T] = Future[Either[Error, T]]
  type FutureResultT[T] = EitherT[Future, Error, T]
}
