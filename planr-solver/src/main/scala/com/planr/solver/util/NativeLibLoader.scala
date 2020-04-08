package com.planr.solver.util

import play.api.Logger

import scala.util.Try

object NativeLibLoader {

  private val logger    = Logger(this.getClass)
  private val separator = System.getProperty("file.separator")

  val DEVELOPMENT: String = separator + "target" + separator + "scala-2.13"
  val PRODUCTION:  String = separator + "app"

  def init(path: String): Boolean = {
    val fullPath = System.getProperty("user.dir") + getLibrary(path)
    Try {
      System.load(fullPath)
    }.fold(
      _ => {
        logger.error(s"Failed loading native library with path: $fullPath")
        false
      },
      _ => {
        logger.info(s"Success loading native library with path: $fullPath")
        true
      }
    )
  }

  private def getLibrary(path: String): String = {
    val os = System.getProperty("os.name").toLowerCase
    if (os.contains("nix") || os.contains("nux")) path + separator + "lib" + separator + "natives" + separator + "libjniortools.so"
    else if (os.contains("win")) path + separator + "lib" + separator + "natives" + separator + "jniortools.dll"
    else ""
  }

}
