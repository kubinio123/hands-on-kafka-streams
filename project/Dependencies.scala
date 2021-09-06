import sbt._

object Dependencies {

  object Versions {
    val kafka = "2.8.0"
    val kafkaAvro = "6.2.0"
    val avro4s = "4.0.10"
    val sttp3 = "3.3.11"
    val circe = "0.14.1"
  }

  object Libs {
    val kafkaClient = "org.apache.kafka" % "kafka-clients" % Versions.kafka
    val kafkaStreams = "org.apache.kafka" % "kafka-streams" % Versions.kafka
    val kafkaStreamsScala = "org.apache.kafka" %% "kafka-streams-scala" % Versions.kafka
    val kafkaAvro = "io.confluent" % "kafka-avro-serializer" % Versions.kafkaAvro
    val kafkaStreamsAvro = "io.confluent" % "kafka-streams-avro-serde" % Versions.kafkaAvro

    val avro4sCore = "com.sksamuel.avro4s" % "avro4s-core_2.13" % Versions.avro4s

    val sttp3Core = "com.softwaremill.sttp.client3" %% "core" % Versions.sttp3
    val sttp3Circe = "com.softwaremill.sttp.client3" %% "circe" % Versions.sttp3
    val circeGeneric = "io.circe" %% "circe-generic" % Versions.circe
  }
}
