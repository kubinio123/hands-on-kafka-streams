import sbt._

object Dependencies {

  object V {
    val kafka = "2.8.0"
    val kafkaAvro = "6.2.0"
    val avro4s = "4.0.10"
    val sttp3 = "3.3.11"
    val circe = "0.14.1"
    val cats = "3.2.5"
  }

  object Libs {
    val kafkaClient = "org.apache.kafka" % "kafka-clients" % V.kafka
    val kafkaStreams = "org.apache.kafka" % "kafka-streams" % V.kafka
    val kafkaStreamsScala = "org.apache.kafka" %% "kafka-streams-scala" % V.kafka
    val kafkaAvro = "io.confluent" % "kafka-avro-serializer" % V.kafkaAvro
    val kafkaStreamsAvro = "io.confluent" % "kafka-streams-avro-serde" % V.kafkaAvro

    val avro4sCore = "com.sksamuel.avro4s" % "avro4s-core_2.13" % V.avro4s

    val sttp3Core = "com.softwaremill.sttp.client3" %% "core" % V.sttp3
    val sttp3Circe = "com.softwaremill.sttp.client3" %% "circe" % V.sttp3
    val circeGeneric = "io.circe" %% "circe-generic" % V.circe

    val catsEffect = "org.typelevel" %% "cats-effect" % V.cats
  }
}
