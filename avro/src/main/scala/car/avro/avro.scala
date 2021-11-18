package car

import car.domain._
import com.sksamuel.avro4s.{AvroSchema, RecordFormat}
import com.softwaremill.tagging._
import org.apache.avro.Schema

package object avro {
  type KeyRFTag
  type KeyRecordFormat[K] = RecordFormat[K] @@ KeyRFTag

  type ValueRFTag
  type ValueRecordFormat[V] = RecordFormat[V] @@ ValueRFTag

  val carIdSchema: Schema = AvroSchema[CarId]
  val carSpeedSchema: Schema = AvroSchema[CarSpeed]
  val carEngineSchema: Schema = AvroSchema[CarEngine]
  val carLocationSchema: Schema = AvroSchema[CarLocation]
  val driverNotificationSchema: Schema = AvroSchema[DriverNotification]

  implicit val carIdRF: KeyRecordFormat[CarId] = RecordFormat[CarId].taggedWith[KeyRFTag]
  implicit val carSpeedRF: ValueRecordFormat[CarSpeed] = RecordFormat[CarSpeed].taggedWith[ValueRFTag]
  implicit val carEngineRF: ValueRecordFormat[CarEngine] = RecordFormat[CarEngine].taggedWith[ValueRFTag]
  implicit val carLocationRF: ValueRecordFormat[CarLocation] = RecordFormat[CarLocation].taggedWith[ValueRFTag]
  implicit val driverNotificationRF: ValueRecordFormat[DriverNotification] = RecordFormat[DriverNotification].taggedWith[ValueRFTag]

  val locationIdSchema: Schema = AvroSchema[LocationId]
  val locationDataSchema: Schema = AvroSchema[LocationData]

  implicit val locationIdRF: KeyRecordFormat[LocationId] = RecordFormat[LocationId].taggedWith[KeyRFTag]
  implicit val locationDataRF: ValueRecordFormat[LocationData] = RecordFormat[LocationData].taggedWith[ValueRFTag]
}
