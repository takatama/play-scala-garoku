name := "garoku"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  "com.github.t3hnar" % "scala-bcrypt_2.10" % "2.3",
  "com.typesafe" %% "play-plugins-mailer" % "2.1.0",
  "postgresql" % "postgresql" % "9.1-901-1.jdbc4"
)     

play.Project.playScalaSettings
