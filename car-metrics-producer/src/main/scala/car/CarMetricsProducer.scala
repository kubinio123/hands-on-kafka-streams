package car

import car.CarMetric._
import car.avro.Avro
import io.confluent.kafka.serializers.{AbstractKafkaAvroSerDeConfig, KafkaAvroSerializer}
import org.apache.avro.generic.IndexedRecord
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}

import java.util.Properties
import scala.annotation.tailrec
import scala.util.Random

object CarMetricsProducer extends App {

  val props = new Properties()
  props.put(ProducerConfig.CLIENT_ID_CONFIG, "car-metrics-producer")
  props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092")
  props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[KafkaAvroSerializer])
  props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[KafkaAvroSerializer])
  props.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://schema-registry:8081")

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

    Thread.sleep(5)

    produce()
  }

  produce()
}
