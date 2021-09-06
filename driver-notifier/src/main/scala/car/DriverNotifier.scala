package car

import car.avro.Avro._
import io.confluent.kafka.streams.serdes.avro.GenericAvroSerde
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StreamsConfig.{APPLICATION_ID_CONFIG, BOOTSTRAP_SERVERS_CONFIG}
import org.apache.kafka.streams.kstream.Consumed
import org.apache.kafka.streams.scala.ImplicitConversions._
import org.apache.kafka.streams.scala._
import org.apache.kafka.streams.scala.kstream.{KStream, KTable}
import org.apache.kafka.streams.scala.serialization.Serdes._

import java.util.Properties
import scala.jdk.CollectionConverters._

object DriverNotifier extends App {

  import Avro4s._

  val props = new Properties()
  props.put(APPLICATION_ID_CONFIG, "driver-notifier")
  props.put(BOOTSTRAP_SERVERS_CONFIG, "kafka:9092")
  props.put("schema.registry.url", "http://schema-registry:8081")

  val serdeProps = Map("schema.registry.url" -> "http://schema-registry:8081")

  val propsJ = serdeProps.asJava
  val avroKeySerde = new GenericAvroSerde
  avroKeySerde.configure(propsJ, true)
  val avroValueSerde = new GenericAvroSerde
  avroValueSerde.configure(propsJ, false)

  implicit val carMetricKeySerde: Serde[CarMetricKey] = avroKeySerde.forCaseClass[CarMetricKey]
  implicit val carMetricValueSerde: Serde[CarMetricValue] = avroValueSerde.forCaseClass[CarMetricValue]
  implicit val carMetricKV: Consumed[CarMetricKey, CarMetricValue] = Consumed.`with`(carMetricKeySerde, carMetricValueSerde)

  val builder: StreamsBuilder = new StreamsBuilder

  val carMetrics: KStream[CarMetricKey, CarMetricValue] = builder.stream("car-metrics")

//  val carLocations = builder.stream("car-locations", Avro4s.consumed[CarLocationKey, CarLocationValue](serdeProps))

  val carMetricsCount: KTable[DriverNotificationKey, Long] = carMetrics.map((key, value) => (DriverNotificationKey(key.carId), s"$value")).groupBy((key, _) => key).count()

  carMetricsCount.toStream.to

  val topology = builder.build()
  val streams = new KafkaStreams(topology, props)

  streams.start()

  Runtime.getRuntime.addShutdownHook(new Thread(() => streams.close()))
}
