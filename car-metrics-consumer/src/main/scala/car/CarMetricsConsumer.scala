package car

import car.avro.Avro
import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG
import io.confluent.kafka.serializers.KafkaAvroDeserializer
import org.apache.avro.generic.IndexedRecord
import org.apache.kafka.clients.consumer.ConsumerConfig._
import org.apache.kafka.clients.consumer.KafkaConsumer

import java.time.Duration
import java.util.Properties
import scala.jdk.CollectionConverters._

object CarMetricsConsumer extends App {

  val props = new Properties()
  props.put(GROUP_ID_CONFIG, "car-metrics-consumer")
  props.put(BOOTSTRAP_SERVERS_CONFIG, "kafka:9092")
  props.put(AUTO_OFFSET_RESET_CONFIG, "earliest")
  props.put(KEY_DESERIALIZER_CLASS_CONFIG, classOf[KafkaAvroDeserializer])
  props.put(VALUE_DESERIALIZER_CLASS_CONFIG, classOf[KafkaAvroDeserializer])
  props.put(SCHEMA_REGISTRY_URL_CONFIG, "http://schema-registry:8081")

  val consumer = new KafkaConsumer[IndexedRecord, IndexedRecord](props)

  consumer.subscribe(Seq("car-metrics").asJava)

  while (true) {
    consumer
      .poll(Duration.ofSeconds(5))
      .asScala
      .map { record =>
        val key = Avro.carMetricKeyRecordFormat.from(record.key())
        val value = Avro.carMetricValueRecordFormat.from(record.value())

        println(key)
        println(value)
      }
  }
}
