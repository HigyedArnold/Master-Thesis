package com.planr.rest.json

import java.io.FileInputStream

import com.planr.api.msg.ErrorCodes._
import com.planr.api.msg.Error
import play.api.libs.json.{Json, Reads}

import scala.util.Try

object JsonUtil {

  def jsonFileToCaseClass[T](resourePath: String)(implicit reads: Reads[T]): Either[Error, T] =
    Try {
      val stream = new FileInputStream(resourePath)
      val value  = Json.parse(stream).asOpt[T]
      stream.close()
      value
    }.fold(
      _ => Left(Error(this.getClass.getName, API__ERROR + JSON_SERIALIZATION__ERROR, "Failed to parse json file!")),
      v => if (v.isDefined) Right(v.get) else Left(Error(this.getClass.getName, API__ERROR + JSON_EMPTY__ERROR, "Failed to parse json file!"))
    )

}
