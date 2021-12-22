package car.drivernotifier

import car.avro._
import car.domain._
import car.drivernotifier.AvroSerdes._
import cats.implicits._
import com.sksamuel.avro4s.BinaryFormat
import com.sksamuel.avro4s.kafka.GenericSerde
import org.apache.kafka.common.utils.Bytes
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StreamsConfig.{APPLICATION_ID_CONFIG, BOOTSTRAP_SERVERS_CONFIG}
import org.apache.kafka.streams.scala.ImplicitConversions._
import org.apache.kafka.streams.scala._
import org.apache.kafka.streams.scala.kstream.{KGroupedStream, KTable, Materialized}
import org.apache.kafka.streams.state.KeyValueStore

import java.util.Properties

object DriverNotifier extends App {

  import DriverNotifierData._

  val props = new Properties()
  props.put(APPLICATION_ID_CONFIG, "driver-notifier")
  props.put(BOOTSTRAP_SERVERS_CONFIG, "kafka:9092")

  val builder: StreamsBuilder = new StreamsBuilder

  val carSpeed: KGroupedStream[CarId, CarSpeed] = builder.stream[CarId, CarSpeed]("car-speed").groupByKey
  val carEngine: KGroupedStream[CarId, CarEngine] = builder.stream[CarId, CarEngine]("car-engine").groupByKey
  val carLocation: KGroupedStream[CarId, CarLocation] = builder.stream[CarId, CarLocation]("car-location").groupByKey
  val locationData: KTable[LocationId, LocationData] = builder.table[LocationId, LocationData]("location-data")

  implicit val carDataSerde: GenericSerde[CarData] = new GenericSerde[CarData](BinaryFormat)

  val carData: KTable[CarId, CarData] = carSpeed
    .cogroup[CarData]({ case (_, speed, agg) => agg.copy(speed = speed.some) })
    .cogroup[CarEngine](carEngine, { case (_, engine, agg) => agg.copy(engine = engine.some) })
    .cogroup[CarLocation](carLocation, { case (_, location, agg) => agg.copy(location = location.some) })
    .aggregate(CarData.empty)

  implicit val carAndLocationDataSerde: GenericSerde[CarAndLocationData] = new GenericSerde[CarAndLocationData](BinaryFormat)

  val carAndLocationData: KTable[CarId, CarAndLocationData] = carData
    .filter({ case (_, carData) => carData.location.isDefined })
    .join[CarAndLocationData, LocationId, LocationData](
      locationData,
      keyExtractor = (carData: CarData) => carData.location.get.locationId,
      joiner = (carData: CarData, locationData: LocationData) => CarAndLocationData(carData, locationData),
      materialized = implicitly[Materialized[CarId, CarAndLocationData, KeyValueStore[Bytes, Array[Byte]]]]
    )

  def print[K, V](k: K, v: V): Unit = println(s"$k -> $v")

  carAndLocationData.toStream.flatMapValues(DriverNotifications(_)).to("driver-notification")

  val topology = builder.build()
  val streams = new KafkaStreams(topology, props)

  streams.start()

  Runtime.getRuntime.addShutdownHook(new Thread(() => streams.close()))
}
