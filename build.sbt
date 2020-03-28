lazy val commonSettings = commonSmlBuildSettings ++ ossPublishSettings ++ acyclicSettings ++ Seq(
  organization := "com.softwaremill.sttp.resilience4s",
  scalaVersion := "2.13.1",
  scalafmtOnCompile := true,
  libraryDependencies ++= Seq(
    compilerPlugin("com.softwaremill.neme" %% "neme-plugin" % "0.0.5")
  ),
  scmInfo := Some(
    ScmInfo(
      url("https://github.com/softwaremill/resilience4s"),
      "git@github.com:softwaremill/resilience4s.git"
    )
  )
)

lazy val circutBreaker = (project in file("circuitbreaker"))
  .settings(commonSettings)
  .settings(
    name := "circuitbreaker",
    libraryDependencies += "io.github.resilience4j" % "resilience4j-circuitbreaker" % "1.3.1"
  )

lazy val rootProject = (project in file("."))
  .settings(commonSettings)
  .settings(publishArtifact := false, name := "resilience4s")
  .aggregate(circutBreaker)