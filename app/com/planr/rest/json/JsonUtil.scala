package com.planr.rest.json

import java.io.FileInputStream

import com.planr.api.msg.ErrorCodes._
import com.planr.api.msg.Error
import play.api.Logger
import play.api.libs.json.{Json, Reads}

import scala.util.Try

object JsonUtil {

  private val logger = Logger(this.getClass)

  def jsonFileToCaseClass[T](resourePath: String)(implicit reads: Reads[T]): Either[Error, T] =
    Try {
      val stream = new FileInputStream(resourePath)
      val value  = Json.parse(stream).asOpt[T]
      stream.close()
      value
    }.fold(
      _ => {
        val err = Error(this.getClass.getName, REST__ERROR + JSON_SERIALIZATION__ERROR, "Failed to parse json file!")
        logger.error(err.toString)
        Left(err)
      },
      v =>
        if (v.isDefined) Right(v.get)
        else {
          val err = Error(this.getClass.getName, REST__ERROR + JSON_EMPTY__ERROR, "Failed to parse json file!")
          logger.error(err.toString)
          Left(err)
        }
    )

}
