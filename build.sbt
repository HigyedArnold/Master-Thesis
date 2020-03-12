/**
 * Copyright (c) 2020 Scalout
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

//#############################################################################
//  COMMANDS
//#############################################################################

addCommandAlias("build",          ";compile;Test/compile")
addCommandAlias("rebuild",        ";clean;compile;Test/compile")

//#############################################################################
//  PROJECTS
//#############################################################################

lazy val root = Project(id = "planr", base = file("."))
  .settings(PublishingSettings.noPublishSettings)
  .settings(Settings.commonSettings)
  .aggregate(
    `planr-core`
  )

//#############################################################################
//  PLANR
//#############################################################################

lazy val `planr-core-deps` = Seq(
  scalaTest,
  logbackClassic
)

lazy val `planr-core` = project
  .settings(PublishingSettings.noPublishSettings)
  .settings(Settings.commonSettings)
  .settings(
    name := "planr-core",
    libraryDependencies ++= `planr-core-deps`.distinct,
  )
  .dependsOn(
  )
  .aggregate(
  )

//#############################################################################
//  DEPENDENCIES
//#############################################################################

lazy val logbackVersion:   String = "1.2.3"
lazy val scalaTestVersion: String = "3.1.1"

//#############################################################################
//  TESTING
//#############################################################################

// https://github.com/scalatest/scalatest/releases
lazy val scalaTest: ModuleID = "org.scalatest" %% "scalatest" % scalaTestVersion withSources ()

//#############################################################################
//  LOGGING
//#############################################################################

// https://github.com/qos-ch/logback/releases
lazy val logbackClassic = "ch.qos.logback" % "logback-classic" % logbackVersion withSources ()