package car

import car.Avro4s._
import car.avro.Avro._
import com.sksamuel.avro4s.BinaryFormat
import com.sksamuel.avro4s.kafka.GenericSerde
import org.apache.kafka.common.utils.Bytes
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StreamsConfig.{APPLICATION_ID_CONFIG, BOOTSTRAP_SERVERS_CONFIG}
import org.apache.kafka.streams.kstream.JoinWindows
import org.apache.kafka.streams.scala.ImplicitConversions._
import org.apache.kafka.streams.scala._
import org.apache.kafka.streams.scala.kstream.KTable
import org.apache.kafka.streams.state.KeyValueStore

import java.time.Duration
import java.time.temporal.ChronoUnit.SECONDS
import java.util.Properties

object DriverNotifier extends App {

  import DriverNotifierData._

  val props = new Properties()
  props.put(APPLICATION_ID_CONFIG, "driver-notifier")
  props.put(BOOTSTRAP_SERVERS_CONFIG, "kafka:9092")
  //props.put("schema.registry.url", "http://schema-registry:8081")

  val builder: StreamsBuilder = new StreamsBuilder

  val carSpeed = builder.stream[CarId, CarSpeed]("car-speed").groupByKey
  val carEngine = builder.stream[CarId, CarEngine]("car-engine").groupByKey
  val carLocation = builder.stream[CarId, CarLocation]("car-location").groupByKey
  val locationData = builder.stream[LocationId, LocationData]("location-data").toTable

  val window = JoinWindows.of(Duration.of(60, SECONDS))

  val carData: KTable[CarId, CarData] = carSpeed
    .cogroup[CarData]({ case (_, speed, agg) => agg.copy(speed = speed) })
    .cogroup[CarEngine](carEngine, { case (_, engine, agg) => agg.copy(engine = engine) })
    .cogroup[CarLocation](carLocation, { case (_, location, agg) => agg.copy(location = location) })
    .aggregate(CarData.empty)
//
//  val carAndLocationData: KTable[CarId, CarAndLocationData] = carData.join[CarAndLocationData, LocationId, LocationData](
//    locationData,
//    keyExtractor = (carData: CarData) => carData.location.locationId,
//    joiner = (carData: CarData, locationData: LocationData) => CarAndLocationData(carData, locationData),
//    materialized = materializedFromSerde[CarId, CarAndLocationData, KeyValueStore[Bytes, Array[Byte]]]
//  )

  carData.toStream
    .map { case (k, v) => (k, DriverNotification(v.toString)) }
    .peek { case (k, v) => println(s"$k -> $v") }
    .to("driver-notification")

  val topology = builder.build()
  val streams = new KafkaStreams(topology, props)

  streams.start()

  Runtime.getRuntime.addShutdownHook(new Thread(() => streams.close()))
}

object DriverNotifierData {
  case class CarData(speed: CarSpeed, engine: CarEngine, location: CarLocation)

  object CarData {
    val empty: CarData = CarData(null, null, null)
  }

  implicit val carDataSerde: GenericSerde[CarData] = new GenericSerde[CarData](BinaryFormat)

  case class CarAndLocationData(carData: CarData, locationData: LocationData)

  implicit val carAndLocationDataSerde: GenericSerde[CarAndLocationData] = new GenericSerde[CarAndLocationData](BinaryFormat)
}
