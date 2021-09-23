package car

import car.avro.Avro
import cats.effect.{ExitCode, IO, IOApp, Resource}
import cats.syntax.all._
import com.sksamuel.avro4s.RecordFormat
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG
import io.confluent.kafka.serializers.KafkaAvroDeserializer
import org.apache.avro.generic.IndexedRecord
import org.apache.kafka.clients.consumer.ConsumerConfig._
import org.apache.kafka.clients.consumer.KafkaConsumer

import java.time.Duration
import scala.jdk.CollectionConverters._

object Consumers extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    import Avro._
    Seq(
      Consume.forever[CarMetricKey, CarMetricValue]("car-metrics"),
      Consume.forever[CarLocationKey, CarLocationValue]("car-locations"),
      Consume.forever[WeatherKey, WeatherValue]("weather"),
      Consume.forever[DriverNotificationKey, DriverNotificationValue]("driver-notifications")
    ).parSequence_.as(ExitCode.Success)
  }
}

object Consume {

  def forever[K >: Null, V >: Null](topic: String)(implicit krf: RecordFormat[K], vrf: RecordFormat[V]): IO[Nothing] =
    Resource
      .make(IO {
        val consumer = new KafkaConsumer[IndexedRecord, IndexedRecord](props.asJava)
        consumer.subscribe(Seq(topic).asJava)
        consumer
      })(c => IO(println(s"[$topic] closing consumer...")) *> IO(c.close()))
      .use { consumer =>
        val consume: IO[Unit] = for {
          records <- IO(consumer.poll(Duration.ofSeconds(5)).asScala.toSeq)
          keyValue = records.map { r => (krf.from(r.key()), vrf.from(r.value())) }
          _ <- keyValue.traverse { case (k, v) => IO(println(s"[$topic] $k => $v")) }
        } yield ()
        consume.foreverM
      }

  val props: Map[String, Object] = Map(
    GROUP_ID_CONFIG -> "car-metrics-consumer",
    BOOTSTRAP_SERVERS_CONFIG -> "kafka:9092",
    AUTO_OFFSET_RESET_CONFIG -> "earliest",
    KEY_DESERIALIZER_CLASS_CONFIG -> classOf[KafkaAvroDeserializer],
    VALUE_DESERIALIZER_CLASS_CONFIG -> classOf[KafkaAvroDeserializer],
    SCHEMA_REGISTRY_URL_CONFIG -> "http://schema-registry:8081"
  )
}
