package car

import car.Avro4s._
import car.avro.Avro._
import cats.implicits._
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StreamsConfig.{APPLICATION_ID_CONFIG, BOOTSTRAP_SERVERS_CONFIG}
import org.apache.kafka.streams.kstream.JoinWindows
import org.apache.kafka.streams.scala.ImplicitConversions._
import org.apache.kafka.streams.scala._
import org.apache.kafka.streams.scala.kstream.KTable

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

  val x: KTable[CarDataKey, CarDataAggregate] = carSpeed
    .cogroup[CarDataAggregate]({ case (_, speed, agg) => agg.copy(speed = speed.some) })
    .cogroup[CarEngineData](carEngine, { case (_, engine, agg) => agg.copy(engine = engine.some) })
    .cogroup[CarLocationData](carLocation, { case (_, location, agg) => agg.copy(location = location.some) })
    .aggregate(CarDataAggregate.empty)

  x.toStream
    .map { case (k, v) => (k, DriverNotification(v.toString)) }
    .peek { case (k, v) => println(s"$k -> $v") }
    .to("driver-notification")

  val topology = builder.build()
  val streams = new KafkaStreams(topology, props)

  streams.start()

  Runtime.getRuntime.addShutdownHook(new Thread(() => streams.close()))
}
