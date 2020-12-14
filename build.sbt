ThisBuild / organization := "net.reasoning"
ThisBuild / version      := "0.0.1"
ThisBuild / scalaVersion := "2.13.4"

// Download source for binary dependencies
bloopExportJarClassifiers in Global := Some(Set("sources"))

import Dependencies._

libraryDependencies ++= Dependencies.Core.dependencies
libraryDependencies ++= Dependencies.Test.dependencies
