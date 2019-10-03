import sbt._

object Dependencies {

  object Core {
    // Versions
    lazy val catsVersion = "2.0.0"

    // Libraries
    val catsCore = "org.typelevel" %% "cats-core" % catsVersion

    val dependencies = Seq(
      catsCore
    )
  }

  object Test {
    // Versions
    lazy val scalaTestVersion = "3.0.8"

    val scalaTest = "org.scalatest" %% "scalatest" % scalaTestVersion

    val dependencies = Seq(
      scalaTest
    ) map (_ % "test")
  }

}