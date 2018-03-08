scalaVersion := "2.12.4"

resolvers ++= Seq (
  Resolver.sonatypeRepo("releases"),
  Resolver.sonatypeRepo("snapshots")
)

libraryDependencies ++= Seq(
  "org.scalacheck" %% "scalacheck" % "1.13.4" % "test"
)

scalacOptions ++= Seq("-deprecation", "-feature", "-language:_", "-Ypartial-unification")

