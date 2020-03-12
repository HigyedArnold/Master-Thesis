package com.scalout.core

object ScalaUtil {

  private val separator = System.getProperty("file.separator")

  def init(): Unit = System.load(System.getProperty("user.dir") + ScalaUtil.getLibrary)

  private def getLibrary: String = {
    val os = System.getProperty("os.name").toLowerCase
    if (os.contains("nix") || os.contains("nux")) separator + "target" + separator + "natives" + separator + "linux_64" + separator + "libjniortools.so"
    else if (os.contains("win")) separator + "target" + separator + "natives" + separator + "windows_64" + separator + "jniortools.dll"
    else ""
  }

}
