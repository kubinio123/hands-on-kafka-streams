package car

import car.Avro4s._
import car.avro.Avro._
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StreamsConfig.{APPLICATION_ID_CONFIG, BOOTSTRAP_SERVERS_CONFIG}
import org.apache.kafka.streams.kstream.JoinWindows
import org.apache.kafka.streams.scala.ImplicitConversions._
import org.apache.kafka.streams.scala._
import org.apache.kafka.streams.scala.kstream.Materialized

import java.time.Duration
import java.time.temporal.ChronoUnit.SECONDS
import java.util.Properties

object DriverNotifier extends App {

  val props = new Properties()
  props.put(APPLICATION_ID_CONFIG, "driver-notifier")
  props.put(BOOTSTRAP_SERVERS_CONFIG, "kafka:9092")
  props.put("schema.registry.url", "http://schema-registry:8081")

  val builder: StreamsBuilder = new StreamsBuilder

  val carSpeed = builder.stream[CarDataKey, CarSpeedData]("car-speed").groupByKey
  val carEngine = builder.stream[CarDataKey, CarEngineData]("car-engine").groupByKey
  val carLocation = builder.stream[CarDataKey, CarLocationData]("car-location").groupByKey
  val locationData = builder.stream[LocationDataKey, LocationData]("location-data").toTable

  val window = JoinWindows.of(Duration.of(60, SECONDS))

  case class CarData(speed: CarSpeedData, engine: CarEngineData, location: CarLocationData)

  carSpeed
    .cogroup[CarData]({ case (a, b, agg) => agg.copy(speed = b) })
    .cogroup[CarEngineData](carEngine, { case (a, b, agg) => agg.copy(engine = b) })
    .cogroup[CarLocationData](carLocation, { case (a, b, agg) => agg.copy(location = b) })
    .aggregate(CarData(null, null, null))(Materialized.)

  val topology = builder.build()
  val streams = new KafkaStreams(topology, props)

  streams.start()

  Runtime.getRuntime.addShutdownHook(new Thread(() => streams.close()))
}
