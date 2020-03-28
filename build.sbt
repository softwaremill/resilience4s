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

lazy val core = (project in file("core"))
  .settings(commonSettings)
  .settings(name := "circuitbreaker")

lazy val circuitBreaker = (project in file("circuitBreaker"))
  .settings(commonSettings)
  .settings(
    name := "circuitbreaker",
    libraryDependencies += "io.github.resilience4j" % "resilience4j-circuitbreaker" % "1.3.1"
  )
  .dependsOn(core)

lazy val rateLimiter = (project in file("rateLimiter"))
  .settings(commonSettings)
  .settings(
    name := "ratelimiter",
    libraryDependencies += "io.github.resilience4j" % "resilience4j-ratelimiter" % "1.3.1"
  )
  .dependsOn(core)

lazy val retry = (project in file("retry"))
  .settings(commonSettings)
  .settings(
    name := "retry",
    libraryDependencies += "io.github.resilience4j" % "resilience4j-retry" % "1.3.1"
  )
  .dependsOn(core)

lazy val bulkhead = (project in file("bulkhead"))
  .settings(commonSettings)
  .settings(
    name := "bulkhead",
    libraryDependencies += "io.github.resilience4j" % "resilience4j-bulkhead" % "1.3.1"
  )
  .dependsOn(core)

lazy val timeLimiter = (project in file("timeLimiter"))
  .settings(commonSettings)
  .settings(
    name := "timeLimiter",
    libraryDependencies ++= Seq(
      "io.github.resilience4j" % "resilience4j-timelimiter" % "1.3.1",
      "org.typelevel" %% "cats-effect" % "2.1.2"
    )
  )
  .dependsOn(core)

lazy val rootProject = (project in file("."))
  .settings(commonSettings)
  .settings(publishArtifact := false, name := "resilience4s")
  .aggregate(circuitBreaker, core, rateLimiter, retry, bulkhead, timeLimiter)
