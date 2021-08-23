package car

import io.confluent.kafka.serializers.{AbstractKafkaAvroSerDeConfig, KafkaAvroDeserializer, KafkaAvroDeserializerConfig}
import car.avro.Avro
import org.apache.avro.generic.IndexedRecord
import org.apache.kafka.clients.consumer.{ConsumerConfig, KafkaConsumer}

import java.time.Duration
import java.util.Properties
import scala.jdk.CollectionConverters._

object CarMetricsConsumer extends App {

  val props = new Properties()
  props.put(ConsumerConfig.GROUP_ID_CONFIG, "car-metrics-consumer")
  props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092")
  props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
  props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, classOf[KafkaAvroDeserializer])
  props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, classOf[KafkaAvroDeserializer])
  props.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://schema-registry:8081")

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
