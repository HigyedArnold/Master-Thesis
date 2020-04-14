package com.planr.solver.util

import play.api.Logger

object NativeLibLoader {

  private val logger = Logger(this.getClass)
  val separator: String = System.getProperty("file.separator")

  def init(path: String = ""): Boolean = {
    val fullPath = System.getProperty("user.dir") + getLibrary(path)
    try {
      System.load(fullPath)
      logger.info(s"Success loading native library with path: $fullPath")
      true
    }
    catch {
      case _: Throwable =>
        logger.error(s"Failed loading native library with path: $fullPath")
        false
    }
  }

  private def getLibrary(path: String): String = {
    val os = System.getProperty("os.name").toLowerCase
    if (os.contains("nix") || os.contains("nux")) path + separator + "app" + separator + "natives" + separator + "libjniortools.so"
    else if (os.contains("win")) path + separator + "app" + separator + "natives" + separator + "jniortools.dll"
    else ""
  }

}
