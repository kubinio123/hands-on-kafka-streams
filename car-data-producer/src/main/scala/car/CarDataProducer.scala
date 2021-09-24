package car

import car.Produce.send
import car.avro.Avro
import cats.effect.{ExitCode, IO, IOApp, Resource}
import cats.syntax.all._
import com.sksamuel.avro4s.RecordFormat
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG
import io.confluent.kafka.serializers.KafkaAvroSerializer
import org.apache.avro.generic.IndexedRecord
import org.apache.kafka.clients.producer.ProducerConfig.{
  BOOTSTRAP_SERVERS_CONFIG,
  CLIENT_ID_CONFIG,
  KEY_SERIALIZER_CLASS_CONFIG,
  VALUE_SERIALIZER_CLASS_CONFIG
}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

import scala.concurrent.duration.DurationInt
import scala.jdk.CollectionConverters._

object CarDataProducer extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    import Avro._
    Resource
      .make(IO(new KafkaProducer[IndexedRecord, IndexedRecord](Produce.props.asJava)))(p =>
        IO(println("closing producer...")) *> IO(p.close())
      )
      .use { producer =>
        Seq(
          (IO(RandomData.carSpeed).flatMap(send(producer)("car-speed", _)) *> IO(println("send car speed data...")) *> IO.sleep(
            30.seconds
          )).foreverM,
          (IO(RandomData.carEngine).flatMap(send(producer)("car-engine", _)) *> IO(println("send car engine data...")) *> IO.sleep(
            30.seconds
          )).foreverM,
          (IO(RandomData.carLocation).flatMap(send(producer)("car-location", _)) *> IO(println("send car location data...")) *> IO.sleep(
            30.seconds
          )).foreverM,
          (IO(RandomData.locationData).flatMap(send(producer)("location-data", _)) *> IO(println("send location data...")) *> IO.sleep(
            30.seconds
          )).foreverM
        ).parSequence_.as(ExitCode.Success)
      }
  }
}

object Produce {

  def send[K, V](
      producer: KafkaProducer[IndexedRecord, IndexedRecord]
  )(topic: String, records: Seq[(K, V)])(implicit krf: RecordFormat[K], vrf: RecordFormat[V]): IO[Unit] =
    records.traverse {
      case (k, v) => IO(producer.send(new ProducerRecord[IndexedRecord, IndexedRecord](topic, krf.to(k), vrf.to(v))).get())
    }.void

  val props: Map[String, Object] = Map(
    CLIENT_ID_CONFIG -> "car-metrics-producer",
    BOOTSTRAP_SERVERS_CONFIG -> "kafka:9092",
    KEY_SERIALIZER_CLASS_CONFIG -> classOf[KafkaAvroSerializer],
    VALUE_SERIALIZER_CLASS_CONFIG -> classOf[KafkaAvroSerializer],
    SCHEMA_REGISTRY_URL_CONFIG -> "http://schema-registry:8081"
  )
}
