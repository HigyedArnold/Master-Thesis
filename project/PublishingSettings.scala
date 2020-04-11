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

import sbt.Keys._
import sbt._

/**
  *
  * @author Higyed Arnold, https://github.com/HigyedArnold
  * @since 05 Mar 2020
  *
  */
object PublishingSettings {

  /**
    * All instructions for publishing to sonatype can be found on the sbt-plugin page:
    * http://www.scala-sbt.org/release/docs/Using-Sonatype.html
    */
  def noPublishSettings: Seq[Setting[_]] = Seq(
    publish              := {},
    publishLocal         := {},
    skip in publishLocal := true,
    skip in publish      := true,
    publishArtifact      := false
  )

}
