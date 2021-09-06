import Dependencies._

name := "streams-app"
version := "0.1"

lazy val commonSettings = Seq(
  scalaVersion := "2.13.6",
  resolvers += "Confluent Maven Repository" at "https://packages.confluent.io/maven/"
)

lazy val root = (project in file(".")).aggregate(carMetricsProducer, carMetricsConsumer, avro, domain)

lazy val carMetricsProducer = (project in file("car-metrics-producer"))
  .settings(commonSettings)
  .settings(
    name := "car-metrics-producer",
    libraryDependencies ++= Seq(Libs.kafkaClient, Libs.kafkaAvro)
  )
  .dependsOn(domain, avro)

lazy val carMetricsConsumer = (project in file("car-metrics-consumer"))
  .settings(commonSettings)
  .settings(
    name := "car-metrics-consumer",
    libraryDependencies ++= Seq(Libs.kafkaClient, Libs.kafkaAvro)
  )
  .dependsOn(domain, avro)

lazy val driverNotifier = (project in file("driver-notifier"))
  .settings(commonSettings)
  .settings(
    name := "driver-notifier",
    libraryDependencies ++= Seq(Libs.kafkaStreamsScala, Libs.kafkaStreamsAvro)
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
