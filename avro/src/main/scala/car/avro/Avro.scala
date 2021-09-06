package car.avro

import car._
import com.sksamuel.avro4s.{AvroSchema, RecordFormat}
import org.apache.avro.Schema

object Avro {
  val carMetricKeySchema: Schema = AvroSchema[CarMetricKey]
  val carMetricValueSchema: Schema = AvroSchema[CarMetricValue]

  implicit val carMetricKeyRecordFormat: RecordFormat[CarMetricKey] = RecordFormat[CarMetricKey]
  implicit val carMetricValueRecordFormat: RecordFormat[CarMetricValue] = RecordFormat[CarMetricValue]

  val carLocationKeySchema: Schema = AvroSchema[CarLocationKey]
  val carLocationValueSchema: Schema = AvroSchema[CarLocationValue]

  implicit val carLocationKeyRecordFormat: RecordFormat[CarLocationKey] = RecordFormat[CarLocationKey]
  implicit val carLocationValueRecordFormat: RecordFormat[CarLocationValue] = RecordFormat[CarLocationValue]

  val weatherKeySchema: Schema = AvroSchema[WeatherKey]
  val weatherValueSchema: Schema = AvroSchema[WeatherValue]

  implicit val weatherKeyRecordFormat: RecordFormat[WeatherKey] = RecordFormat[WeatherKey]
  implicit val weatherValueRecordFormat: RecordFormat[WeatherValue] = RecordFormat[WeatherValue]

  val driverNotificationKeySchema: Schema = AvroSchema[DriverNotificationKey]
  val driverNotificationValueSchema: Schema = AvroSchema[DriverNotificationValue]

  implicit val driverNotificationKeyRecordFormat: RecordFormat[DriverNotificationKey] = RecordFormat[DriverNotificationKey]
  implicit val driverNotificationValueRecordFormat: RecordFormat[DriverNotificationValue] = RecordFormat[DriverNotificationValue]
}
