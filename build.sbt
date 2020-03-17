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

import com.mariussoutier.sbt.UnpackKeys

//#############################################################################
//#################################  COMMANDS  ################################
//#############################################################################

addCommandAlias("build", ";clean;compile;Test/compile")
addCommandAlias("rebuild", ";clean;update;compile;Test/compile")

//#############################################################################
//###################################  ROOT  ##################################
//#############################################################################

lazy val `root-deps` = Seq(
  jniortoolswin,
  jniortoolslin
)

lazy val root = Project(id = "planr", base = file("."))
  .enablePlugins(com.mariussoutier.sbt.UnpackPlugin)
  .settings(PublishingSettings.noPublishSettings)
  .settings(Settings.commonSettings)
  .settings(
    UnpackKeys.dependenciesJarDirectory := target.value,
    UnpackKeys.dependencyFilter := {file => file.name.contains("jniortools")}
  )
  .settings(
    libraryDependencies ++= `root-deps`.distinct,
  )
  .aggregate(
    `planr-api`,
    `planr-core`
  )

sourceGenerators in Compile += UnpackKeys.unpackJars

//#############################################################################
//#################################  PROJECTS  ################################
//#############################################################################

lazy val `planr-core-deps` = Seq(
  ortools,
  protobuf,

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

lazy val `planr-api-deps` = Seq()

lazy val `planr-api` = project
  .settings(PublishingSettings.noPublishSettings)
  .settings(Settings.commonSettings)
  .settings(
    name := "planr-api",
    libraryDependencies ++= `planr-api-deps`.distinct,
  )
  .dependsOn(
  )
  .aggregate(
  )

//#############################################################################
//###############################  DEPENDENCIES  ##############################
//#############################################################################

lazy val ortoolsV:  String = "7.5.7466"
lazy val protobufV: String = "3.11.2"

lazy val scalaTestV: String = "3.1.1"

lazy val logbackV:   String = "1.2.3"

//#############################################################################
//###################################  CORE  ##################################
//#############################################################################

// https://github.com/google/or-tools/releases
lazy val ortools: ModuleID = "com.google" %% "ortools" % ortoolsV

// https://github.com/google/or-tools/releases
lazy val jniortoolswin: ModuleID = "com.google" %% "jniortools-win" % ortoolsV
lazy val jniortoolslin: ModuleID = "com.google" %% "jniortools-lin" % ortoolsV

// https://github.com/protocolbuffers/protobuf/releases
lazy val protobuf: ModuleID = "com.google" %% "protobuf" % protobufV

//#############################################################################
//#################################  TESTING  #################################
//#############################################################################

// https://github.com/scalatest/scalatest/releases
lazy val scalaTest: ModuleID = "org.scalatest" %% "scalatest" % scalaTestV withSources ()

//#############################################################################
//#################################  LOGGING  #################################
//#############################################################################

// https://github.com/qos-ch/logback/releases
lazy val logbackClassic = "ch.qos.logback" % "logback-classic" % logbackV withSources ()
