package iot

import io.confluent.kafka.serializers.{AbstractKafkaAvroSerDeConfig, KafkaAvroSerializer}
import iot.avro.Avro
import org.apache.avro.generic.IndexedRecord
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord, RecordMetadata}

import java.util.Properties
import java.util.concurrent.Future

object MeasurementProducer extends App {

  val props = new Properties()
  props.put(ProducerConfig.CLIENT_ID_CONFIG, "measurement-producer")
  props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092")
  props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[KafkaAvroSerializer])
  props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[KafkaAvroSerializer])
  props.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://schema-registry:8081")

  val producer = new KafkaProducer[IndexedRecord, IndexedRecord](props)

  val key = MeasurementKey(1)
  val value = MeasurementValue(1, "m", 20)

  val send: Future[RecordMetadata] = producer.send(
    new ProducerRecord[IndexedRecord, IndexedRecord](
      "measurements",
      Avro.measurementKeyRecordFormat.to(key),
      Avro.measurementValueRecordFormat.to(value)
    )
  )

  println(send.get())
}
