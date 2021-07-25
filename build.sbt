import Dependencies._

name := "streams-app"
version := "0.1"

lazy val commonSettings = Seq(
  scalaVersion := "2.13.6",
  resolvers += "Confluent Maven Repository" at "https://packages.confluent.io/maven/"
)

lazy val root = (project in file(".")).aggregate(domain)

lazy val measurementProducer = (project in file("measurement-producer"))
  .settings(commonSettings)
  .settings(
    name := "measurement-producer",
    libraryDependencies ++= Seq(Libs.kafkaClient, Libs.kafkaAvro)
  )
  .dependsOn(domain, avro)

lazy val measurementConsumer = (project in file("measurement-consumer"))
  .settings(commonSettings)
  .settings(
    name := "measurement-consumer",
    libraryDependencies ++= Seq(Libs.kafkaClient, Libs.kafkaAvro)
  )
  .dependsOn(domain, avro)

lazy val avro = (project in file("avro"))
  .settings(commonSettings)
  .settings(
    name := "avro",
    libraryDependencies ++= Seq(Libs.avro4sCore, Libs.sttp3Core, Libs.sttp3Circe, Libs.circeGeneric)
  )
  .dependsOn(domain)

lazy val domain = (project in file("domain")).settings(commonSettings)