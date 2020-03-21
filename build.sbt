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

import com.mariussoutier.sbt._

//#############################################################################
//#################################  COMMANDS  ################################
//#############################################################################

addCommandAlias("build", ";clean;compile;Test/compile")

//#############################################################################
//##################################  BUILD  ##################################
//#############################################################################

enablePlugins(UnpackPlugin, DockerPlugin)

def getPathScalaVersion: String = "scala-2.13"
def getPathJni:          String = "lib"

UnpackKeys.dependenciesJarDirectory := target.value / getPathScalaVersion / getPathJni
UnpackKeys.dependencyFilter := { file =>
  file.name.contains("jniortools")
}
sourceGenerators in (Compile, unpackJars) += UnpackKeys.unpackJars

dockerfile in docker := {
  val artifact: File = assembly.value
  val artifactTargetPath = s"/app/${artifact.name}"
  val jni: File = target.value / getPathScalaVersion / getPathJni
  val libTargetPath = s"/app/${jni.name}"

  new Dockerfile {
    from("openjdk:8-jre")
    copy(artifact, artifactTargetPath)
    copy(jni, libTargetPath)
    entryPoint("java", "-jar", artifactTargetPath)
  }
}

// Image options
imageNames in docker := Seq(
  ImageName(s"${organization.value}/${name.value}:latest"),
  ImageName(
    repository = name.value,
    tag        = Some("v" + version.value),
  ),
)

// Build options
//buildOptions in docker := BuildOptions(
//  cache = false,
//  removeIntermediateContainers = BuildOptions.Remove.Always,
//  pullBaseImage = BuildOptions.Pull.Always
//)

//#############################################################################
//###################################  ROOT  ##################################
//#############################################################################

lazy val `root-deps` = Seq(
  jniortoolswin,
  jniortoolslin,
)

lazy val root = (project in file("."))
  .settings(Settings.commonSettings)
  .settings(
    name := "planr-main",
    libraryDependencies ++= `root-deps`.distinct,
    mainClass := Some("com.planr.main.Main"),
  )
  .dependsOn(
    `planr-core`,
  )
  .aggregate(
    `planr-core`,
  )

//#############################################################################
//#################################  PROJECTS  ################################
//#############################################################################

lazy val `planr-core-deps` = Seq(
  ortools,
  protobuf,
  scalaTest,
  logbackClassic,
)

lazy val `planr-core` = (project in file("planr-core"))
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
//###############################  DEPENDENCIES  ##############################
//#############################################################################

lazy val ortoolsV:  String = "7.5.7466"
lazy val protobufV: String = "3.11.2"

lazy val scalaTestV: String = "3.1.1"

lazy val logbackV: String = "1.2.3"

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
