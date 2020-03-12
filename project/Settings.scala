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

import sbt._
import Keys._

/**
  *
  * @author Higyed Arnold, https://github.com/HigyedArnold
  * @since 05 Mar 2020
  *
  */
object Settings {
  lazy val organizationName: String = "com.planr"

  lazy val planrHomepage: String = "https://github.com/HigyedArnold/Master-Thesis"

  def commonSettings: Seq[Setting[_]] =
    Seq(
      organization := organizationName,
      homepage     := Some(url(planrHomepage)),
      resolvers ++= Seq(
        Resolver.mavenLocal,
        Resolver.sonatypeRepo("public"),
        Resolver.sonatypeRepo("snapshots"),
      ),
    ) ++ CompilerSettings.compilerSettings
}
