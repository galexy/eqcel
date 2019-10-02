scalaVersion := "2.13.1"

// Download source for binary dependencies
bloopExportJarClassifiers in Global := Some(Set("sources"))

libraryDependencies += "org.typelevel" %% "cats-core" % "2.0.0"