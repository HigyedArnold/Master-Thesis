package com.planr.core.util

object NativeLibLoader {

  private val separator = System.getProperty("file.separator")

  val DEVELOPMENT:  String = separator + "target" + separator + "scala-2.13"
  val PRODUCTION: String = separator + "app"

  def init(path: String): Unit = System.load(System.getProperty("user.dir") + getLibrary(path))

  private def getLibrary(path: String): String = {
    val os = System.getProperty("os.name").toLowerCase
    if (os.contains("nix") || os.contains("nux")) path + separator + "lib" + separator + "natives" + separator + "libjniortools.so"
    else if (os.contains("win")) path + separator + "lib" + separator + "natives" + separator + "jniortools.dll"
    else ""
  }

}
