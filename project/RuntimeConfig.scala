/**
  * Copyright (c) 2020 planr
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  * http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */

/**
  *
  * @author Higyed Arnold, https://github.com/HigyedArnold
  * @since 31 Mar 2020
  *
  */
object RuntimeConfig {

  def debugOptions(inDevMode: Boolean): Seq[String] =
    if (inDevMode) Seq("-jvm-debug 5005")
    else Seq()

  def javaRuntimeOptions: Seq[String] =
    Seq(
      // Memory options
      "-Xmx1024m",
      "-Xms512m",
      // Garbage Collector options
      "-XX:+UseParallelGC",
      "-XX:ParallelGCThreads=4",
      // Error options
      "-XX:+HeapDumpOnOutOfMemoryError",
      "-XX:+CrashOnOutOfMemoryError",
      s"-XX:ErrorFile=${LogConfig.logDir}/fatal.log"
    )
}
