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
//##################################  BUILD  ##################################
//#############################################################################

//---------------------------------  COMMANDS  --------------------------------

addCommandAlias("build", ";clean;compile;Test/compile")

//---------------------------------  NATIVES  ---------------------------------

def getPathScalaVersion: String = "scala-2.13"
def getPathJni:          String = "lib"

UnpackKeys.dependenciesJarDirectory := target.value / getPathScalaVersion / getPathJni
UnpackKeys.dependencyFilter := { file =>
  file.name.contains("jniortools")
}

sourceGenerators in (Compile, unpackJars) += UnpackKeys.unpackJars

//----------------------------------  DOCKER  ---------------------------------

dockerfile in docker := {
  val artifact: File = assembly.value
  val artifactTargetPath = s"/app/${artifact.name}"
  val jni: File = target.value / getPathScalaVersion / getPathJni
  val libTargetPath = s"/app/${jni.name}"

  new Dockerfile {
    from("openjdk:8-jre")
    copy(artifact, artifactTargetPath)
    copy(jni, libTargetPath)
    run("mkdir", "-p", s"/app/${LogConfig.logDir}")
    entryPoint("java", "-jar", artifactTargetPath)
  }
}

// Image options
imageNames in docker := Seq(
  ImageName(s"${organization.value}/${name.value}:latest"),
  ImageName(
    repository = name.value,
    tag        = Some("v" + version.value)
  )
)

// Build options
//buildOptions in docker := BuildOptions(
//  cache = false,
//  removeIntermediateContainers = BuildOptions.Remove.Always,
//  pullBaseImage = BuildOptions.Pull.Always
//)

//-----------------------------------  JVM  -----------------------------------

// To fork all test tasks and run tasks
fork := true

javaOptions ++= RuntimeConfig.javaRuntimeOptions
javaOptions ++= RuntimeConfig.debugOptions(false)

//---------------------------------  LOGGING  ---------------------------------

LogConfig.logDirKey := LogConfig.logDir

//#############################################################################
//###################################  ROOT  ##################################
//#############################################################################

lazy val `root-deps` = Seq(
  guice,
  scalaGuice,
  jniortoolswin,
  jniortoolslin
)

lazy val root = (project in file("."))
  .settings(Settings.commonSettings)
  .settings(
    name := "planr-main",
    libraryDependencies ++= `root-deps`.distinct
  )
  .enablePlugins(UnpackPlugin, sbtdocker.DockerPlugin, PlayService, PlayLayoutPlugin)
  .dependsOn(
    `planr-api`
  )
  .aggregate(
    `planr-api`
  )

//#############################################################################
//#################################  PROJECTS  ################################
//#############################################################################

//-----------------------------------  API  -----------------------------------

lazy val `planr-api-deps` = Seq(
  playJson,
  logback   % Test,
  scalaTest % Test
)

lazy val `planr-api` = (project in file("planr-api"))
  .settings(PublishingSettings.noPublishSettings)
  .settings(Settings.commonSettings)
  .settings(
    name := "planr-api",
    libraryDependencies ++= `planr-api-deps`.distinct
  )
  .dependsOn(
    )
  .aggregate(
    )

//-----------------------------------  CORE  ----------------------------------

lazy val `planr-core-deps` = Seq(
  ortools,
  protobuf,
  scalaTest
)

lazy val `planr-core` = (project in file("planr-core"))
  .settings(PublishingSettings.noPublishSettings)
  .settings(Settings.commonSettings)
  .settings(
    name := "planr-core",
    libraryDependencies ++= `planr-core-deps`.distinct
  )
  .dependsOn(
    )
  .aggregate(
    )

//#############################################################################
//###############################  DEPENDENCIES  ##############################
//#############################################################################

lazy val playV:       String = "2.8.1"
lazy val scalaGuiceV: String = "4.2.6"

lazy val ortoolsV:  String = "7.5.7466"
lazy val protobufV: String = "3.11.2"

lazy val scalaTestV: String = "3.1.1"

//-----------------------------------  ROOT  ----------------------------------

// https://github.com/codingwell/scala-guice/releases
lazy val scalaGuice: ModuleID = "net.codingwell" %% "scala-guice" % scalaGuiceV withSources ()

//-----------------------------------  API  -----------------------------------

// https://github.com/playframework/play-json/releases
lazy val playJson: ModuleID = "com.typesafe.play" %% "play-json" % playV withSources ()

//-----------------------------------  CORE  ----------------------------------

// https://github.com/google/or-tools/releases
lazy val ortools: ModuleID = "com.google" %% "ortools" % ortoolsV

// https://github.com/google/or-tools/releases
lazy val jniortoolswin: ModuleID = "com.google" %% "jniortools-win" % ortoolsV
lazy val jniortoolslin: ModuleID = "com.google" %% "jniortools-lin" % ortoolsV

// https://github.com/protocolbuffers/protobuf/releases
lazy val protobuf: ModuleID = "com.google" %% "protobuf" % protobufV

//----------------------------------  TESTING  --------------------------------

// https://github.com/scalatest/scalatest/releases
lazy val scalaTest: ModuleID = "org.scalatest" %% "scalatest" % scalaTestV withSources ()
