package car

import car.avro.Avro
import io.confluent.kafka.streams.serdes.avro.GenericAvroSerde
import org.apache.avro.generic.GenericRecord
import org.apache.kafka.streams.StreamsConfig.{APPLICATION_ID_CONFIG, BOOTSTRAP_SERVERS_CONFIG}
import org.apache.kafka.streams.kstream.{Consumed, KStream}
import org.apache.kafka.streams.{KafkaStreams, StreamsBuilder}

import java.util.{Collections, Properties}

object DriverNotifier extends App {

  val props = new Properties()
  props.put(APPLICATION_ID_CONFIG, "driver-notifier")
  props.put(BOOTSTRAP_SERVERS_CONFIG, "kafka:9092")
  props.put("schema.registry.url", "http://schema-registry:8081")

  val serdeProps = Collections.singletonMap("schema.registry.url", "http://schema-registry:8081")

  val consumedWithAvro = {
    val avroKeySerde = new GenericAvroSerde
    avroKeySerde.configure(serdeProps, true)

    val avroValueSerde = new GenericAvroSerde
    avroValueSerde.configure(serdeProps, false)

    Consumed.`with`(avroKeySerde, avroValueSerde)
  }

  val builder = new StreamsBuilder

  val carMetrics: KStream[GenericRecord, GenericRecord] = builder.stream("car-metrics", consumedWithAvro)

  val carLocations: KStream[GenericRecord, GenericRecord] = builder.stream("car-locations", consumedWithAvro)

  carMetrics.foreach((k, v) => {
    val carMetricKey = Avro.carMetricKeyRecordFormat.from(k)
    val carMetricValue = Avro.carLocationValueRecordFormat.from(v)

    println(s"$carMetricKey => $carMetricValue")
  })

  val topology = builder.build()
  val streams = new KafkaStreams(topology, props)

  streams.start()

  Runtime.getRuntime.addShutdownHook(new Thread(() => streams.close()))
}
