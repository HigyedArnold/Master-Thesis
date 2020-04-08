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

addCommandAlias("build", ";clean;compile;Test/compile;unpackJars")

//---------------------------------  ASSEMBLY  --------------------------------

mainClass in assembly := Some("play.core.server.ProdServerStart")
fullClasspath in assembly += Attributed.blank(PlayKeys.playPackageAssets.value)

assembly / assemblyMergeStrategy := {
  case manifest if manifest.contains("MANIFEST.MF") =>
    // We don't need manifest files since sbt-assembly will create one with the given settings
    MergeStrategy.discard
  case referenceOverrides if referenceOverrides.contains("reference-overrides.conf") =>
    // Keep the content for all reference-overrides.conf files
    MergeStrategy.concat
  case PathList("module-info.class") => MergeStrategy.discard
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}

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
    run("mkdir", "-p", s"/app/logs")
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

//#############################################################################
//###################################  ROOT  ##################################
//#############################################################################

lazy val `root-deps` = Seq(
  guice,
  scalaGuice,
  ortools,
  jniortoolswin,
  jniortoolslin,
  scalaTest
)

lazy val root = (project in file("."))
  .settings(PublishingSettings.noPublishSettings)
  .settings(Settings.commonSettings)
  .settings(
    name := "planr",
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
  catsCore
)

lazy val `planr-api` = (project in file("planr-api"))
  .settings(PublishingSettings.noPublishSettings)
  .settings(Settings.commonSettings)
  .settings(
    name := "planr-api",
    libraryDependencies ++= `planr-api-deps`.distinct
  )

//---------------------------------  GATLING  ---------------------------------

lazy val `planr-gatling-deps` = Seq(
  gatlingHighcharts,
  gatlingFramework
)

lazy val `planr-gatling` = (project in file("planr-gatling"))
  .settings(PublishingSettings.noPublishSettings)
  .settings(Settings.commonSettings)
  .enablePlugins(GatlingPlugin)
  .settings(
    name         := "planr-gatling",
    scalaVersion := "2.12.10",
    libraryDependencies ++= `planr-gatling-deps`.distinct
  )

//#############################################################################
//###############################  DEPENDENCIES  ##############################
//#############################################################################

lazy val scalaGuiceV: String = "4.2.6"
lazy val ortoolsV:    String = "7.5.7466"
lazy val catsV:       String = "2.1.1"
lazy val scalaTestV:  String = "3.1.1"
lazy val gatlingV:    String = "3.3.1"

//-----------------------------------  API  -----------------------------------

// https://github.com/typelevel/cats/releases
lazy val catsCore: ModuleID = "org.typelevel" %% "cats-core" % catsV withSources ()

//-----------------------------------  ROOT  ----------------------------------

// https://github.com/codingwell/scala-guice/releases
lazy val scalaGuice: ModuleID = "net.codingwell" %% "scala-guice" % scalaGuiceV withSources ()

// https://github.com/google/or-tools/releases
lazy val ortools:       ModuleID = "com.google" %% "ortools"        % ortoolsV
lazy val jniortoolswin: ModuleID = "com.google" %% "jniortools-win" % ortoolsV
lazy val jniortoolslin: ModuleID = "com.google" %% "jniortools-lin" % ortoolsV

//----------------------------------  TESTING  --------------------------------

// https://github.com/scalatest/scalatest/releases
lazy val scalaTest: ModuleID = "org.scalatest" %% "scalatest" % scalaTestV % Test withSources ()

// https://gatling.io/docs/current/extensions/sbt_plugin/
lazy val gatlingHighcharts: ModuleID = "io.gatling.highcharts" % "gatling-charts-highcharts" % gatlingV % Test withSources ()
lazy val gatlingFramework:  ModuleID = "io.gatling"            % "gatling-test-framework"    % gatlingV % Test withSources ()
