package com.planr.core.util

object NativeLibLoader {

  private val separator = System.getProperty("file.separator")

  def init(): Unit = System.load(System.getProperty("user.dir") + getLibrary)

  private def getLibrary: String = {
    val os = System.getProperty("os.name").toLowerCase
    if (os.contains("nix") || os.contains("nux")) separator + "target" + separator + "natives" + separator + "libjniortools.so"
    else if (os.contains("win")) separator + "target" + separator + "natives" + separator + "jniortools.dll"
    else ""
  }

}
