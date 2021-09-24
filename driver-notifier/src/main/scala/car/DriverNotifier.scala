package car

import car.Avro4s._
import car.avro.Avro._
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StreamsConfig.{APPLICATION_ID_CONFIG, BOOTSTRAP_SERVERS_CONFIG}
import org.apache.kafka.streams.scala.ImplicitConversions._
import org.apache.kafka.streams.scala._
import org.apache.kafka.streams.scala.kstream.KStream

import java.util.Properties

object DriverNotifier extends App {

  val props = new Properties()
  props.put(APPLICATION_ID_CONFIG, "driver-notifier")
  props.put(BOOTSTRAP_SERVERS_CONFIG, "kafka:9092")
  props.put("schema.registry.url", "http://schema-registry:8081")

  val builder: StreamsBuilder = new StreamsBuilder

  val carSpeed: KStream[CarDataKey, CarSpeedData] = builder.stream("car-speed")
  val carEngine: KStream[CarDataKey, CarEngineData] = builder.stream("car-engine")
  val carLocation: KStream[CarDataKey, CarLocationData] = builder.stream("car-location")
  val locationData: KStream[LocationDataKey, LocationData] = builder.stream("location-data")

  val topology = builder.build()
  val streams = new KafkaStreams(topology, props)

  streams.start()

  Runtime.getRuntime.addShutdownHook(new Thread(() => streams.close()))
}
