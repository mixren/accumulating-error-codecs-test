val Http4sVersion = "0.23.6"
val CirceVersion = "0.14.1"
val MunitVersion = "0.7.29"
val LogbackVersion = "1.2.8"

lazy val root = (project in file("."))
  .settings(
    organization := "io.github.mixren",
    name := "accumulating-error-codecs-test",
    version := "0.1",
    scalaVersion := "2.13.7",
    libraryDependencies ++= Seq(
      "org.http4s"      %% "http4s-ember-server"  % Http4sVersion,
      "org.http4s"      %% "http4s-circe"         % Http4sVersion,
      "org.http4s"      %% "http4s-dsl"           % Http4sVersion,
      "io.circe"        %% "circe-generic"        % CirceVersion,
      "io.circe"        %% "circe-parser"         % CirceVersion,
      "io.circe"        %% "circe-generic-extras" % "0.14.1",
      "org.scalatest"   %% "scalatest"            % "3.2.9"       % Test,
      "ch.qos.logback"  %  "logback-classic"      % LogbackVersion,
      "org.scalameta"   %% "svm-subs"             % "20.2.0",
    ),
    addCompilerPlugin("org.typelevel" %% "kind-projector"     % "0.13.2" cross CrossVersion.full),
    addCompilerPlugin("com.olegpy"    %% "better-monadic-for" % "0.3.1"),
    testFrameworks += new TestFramework("munit.Framework")
  )

