package car.avro

import car._
import com.sksamuel.avro4s.{AvroSchema, RecordFormat}
import com.softwaremill.tagging._
import org.apache.avro.Schema

object Avro {
  type KeyRFTag
  type KeyRecordFormat[K] = RecordFormat[K] @@ KeyRFTag

  type ValueRFTag
  type ValueRecordFormat[V] = RecordFormat[V] @@ ValueRFTag

  val carDataKeySchema: Schema = AvroSchema[CarDataKey]
  val carSpeedDataSchema: Schema = AvroSchema[CarSpeedData]
  val carEngineDataSchema: Schema = AvroSchema[CarEngineData]
  val carLocationDataSchema: Schema = AvroSchema[CarLocationData]
  val driverNotificationSchema: Schema = AvroSchema[DriverNotification]

  implicit val carDataKeyRF: KeyRecordFormat[CarDataKey] = RecordFormat[CarDataKey].taggedWith[KeyRFTag]
  implicit val carSpeedDataRF: ValueRecordFormat[CarSpeedData] = RecordFormat[CarSpeedData].taggedWith[ValueRFTag]
  implicit val carEngineDataRF: ValueRecordFormat[CarEngineData] = RecordFormat[CarEngineData].taggedWith[ValueRFTag]
  implicit val carDataAggregateRF: ValueRecordFormat[CarDataAggregate] = RecordFormat[CarDataAggregate].taggedWith[ValueRFTag]
  implicit val carLocationDataRF: ValueRecordFormat[CarLocationData] = RecordFormat[CarLocationData].taggedWith[ValueRFTag]
  implicit val driverNotificationRF: ValueRecordFormat[DriverNotification] = RecordFormat[DriverNotification].taggedWith[ValueRFTag]

  val locationDataKeySchema: Schema = AvroSchema[LocationDataKey]
  val locationDataSchema: Schema = AvroSchema[LocationData]

  implicit val locationDataKeyRF: KeyRecordFormat[LocationDataKey] = RecordFormat[LocationDataKey].taggedWith[KeyRFTag]
  implicit val locationDataRF: ValueRecordFormat[LocationData] = RecordFormat[LocationData].taggedWith[ValueRFTag]
}
