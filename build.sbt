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
  .settings(name := "core")

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

lazy val cache = (project in file("cache"))
  .settings(commonSettings)
  .settings(
    name := "cache",
    libraryDependencies += "io.github.resilience4j" % "resilience4j-cache" % "1.3.1"
  )
  .dependsOn(core)

lazy val all = (project in file("all"))
  .settings(commonSettings)
  .settings(
    name := "all"
  )
  .dependsOn(bulkhead, cache, circuitBreaker, rateLimiter, retry, timeLimiter)

lazy val cats = (project in file("implementations/cats"))
  .settings(commonSettings)
  .settings(
    name := "cats",
    libraryDependencies += "org.typelevel" %% "cats-effect" % "2.1.2"
  )
  .dependsOn(core)

lazy val monix = (project in file("implementations/monix"))
  .settings(commonSettings)
  .settings(
    name := "monix",
    libraryDependencies += "io.monix" %% "monix" % "3.1.0"
  )
  .dependsOn(core)

lazy val zio = (project in file("implementations/zio"))
  .settings(commonSettings)
  .settings(
    name := "zio",
    libraryDependencies += "dev.zio" %% "zio" % "1.0.0-RC17"
  )
  .dependsOn(core)

lazy val docs = (project in file("generated-docs")) // important: it must not be docs/
  .settings(commonSettings)
  .settings(publishArtifact := false, name := "docs")
  .dependsOn(all, cats, zio, monix)
  .enablePlugins(MdocPlugin, DocusaurusPlugin)
  .settings(
    mdocIn := file("docs-sources"),
    moduleName := "resilience4s-docs",
    mdocVariables := Map(
      "VERSION" -> version.value
    ),
    mdocOut := file(".")
  )

lazy val rootProject = (project in file("."))
  .settings(commonSettings)
  .settings(publishArtifact := false, name := "resilience4s")
  .aggregate(circuitBreaker, core, rateLimiter, retry, bulkhead, timeLimiter, cache, all, cats, monix, zio, docs)
