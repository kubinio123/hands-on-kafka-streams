package car

import car.CarMetric._
import car.avro.Avro
import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG
import io.confluent.kafka.serializers.KafkaAvroSerializer
import org.apache.avro.generic.IndexedRecord
import org.apache.kafka.clients.producer.ProducerConfig.{
  BOOTSTRAP_SERVERS_CONFIG,
  CLIENT_ID_CONFIG,
  KEY_SERIALIZER_CLASS_CONFIG,
  VALUE_SERIALIZER_CLASS_CONFIG
}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

import java.util.Properties
import scala.annotation.tailrec
import scala.util.Random
import scala.concurrent.duration.DurationInt

object CarMetricsProducer extends App {

  val props = new Properties()
  props.put(CLIENT_ID_CONFIG, "car-metrics-producer")
  props.put(BOOTSTRAP_SERVERS_CONFIG, "kafka:9092")
  props.put(KEY_SERIALIZER_CLASS_CONFIG, classOf[KafkaAvroSerializer])
  props.put(VALUE_SERIALIZER_CLASS_CONFIG, classOf[KafkaAvroSerializer])
  props.put(SCHEMA_REGISTRY_URL_CONFIG, "http://schema-registry:8081")

  val producer = new KafkaProducer[IndexedRecord, IndexedRecord](props)

  val carIds = Seq(1, 2, 3)

  val cities = Seq("Wroclaw", "Cracow", "Warsaw")

  @tailrec
  def produce(): Unit = {
    carIds.foreach { carId =>
      val metrics = Seq(
        (Speed, Random.between(100, 130)),
        (EngineRpm, Random.between(2000, 2100)),
        (TirePressure, Random.between(1, 2))
      )

      metrics.foreach {
        case (metric, value) =>
          producer
            .send(
              new ProducerRecord[IndexedRecord, IndexedRecord](
                "car-metrics",
                Avro.carMetricKeyRecordFormat.to(CarMetricKey(carId, metric)),
                Avro.carMetricValueRecordFormat.to(CarMetricValue(value))
              )
            )
            .get()
      }

      val city = cities(Random.nextInt(cities.length))

      producer
        .send(
          new ProducerRecord[IndexedRecord, IndexedRecord](
            "car-locations",
            Avro.carLocationKeyRecordFormat.to(CarLocationKey(carId)),
            Avro.carLocationValueRecordFormat.to(CarLocationValue(city))
          )
        )
        .get()
    }

    cities.foreach { city =>
      val temperature = Random.between(10, 30)
      val isStorming = Random.nextBoolean()

      producer
        .send(
          new ProducerRecord[IndexedRecord, IndexedRecord](
            "weather",
            Avro.weatherKeyRecordFormat.to(WeatherKey(city)),
            Avro.weatherValueRecordFormat.to(WeatherValue(temperature, isStorming))
          )
        )
        .get()
    }

    Thread.sleep(1000)

    produce()
  }

  produce()
}
