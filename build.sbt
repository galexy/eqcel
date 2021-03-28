ThisBuild / organization := "net.reasoning"
ThisBuild / version      := "0.0.1"
ThisBuild / scalaVersion := "2.13.4"

// Download source for binary dependencies
bloopExportJarClassifiers in Global := Some(Set("sources"))

import Dependencies._

lazy val commonDependencies =
    Dependencies.Core.dependencies ++
    Dependencies.Test.dependencies

lazy val global = project
  .in(file("."))
  .aggregate(
     eqcel,
     eqcel_google
  )

lazy val eqcel = project
    .settings(
        libraryDependencies ++= commonDependencies
    )

lazy val eqcel_google = project
    .settings(
      libraryDependencies ++=
        commonDependencies ++
        Dependencies.GoogleSheets.dependencies
    )
    .dependsOn(eqcel)
