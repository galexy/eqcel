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

  object GoogleSheets {
    // Versions
    lazy val googleApiVersion = "1.25.0"
    lazy val googleSheetsRev = "v4-rev581-1.25.0"

    // Libraries
    val googleApi = "com.google.api-client" % "google-api-client" % googleApiVersion
    val googleOauthClient = "com.google.oauth-client" % "google-oauth-client-jetty" % googleApiVersion
    val googleSheets = "com.google.apis" % "google-api-services-sheets" % googleSheetsRev

    val dependencies = Seq(
      googleApi,
      googleOauthClient,
      googleSheets
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
