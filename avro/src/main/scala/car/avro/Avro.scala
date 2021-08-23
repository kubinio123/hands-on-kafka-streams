package car.avro

import car._
import com.sksamuel.avro4s.{AvroSchema, RecordFormat}
import org.apache.avro.Schema

object Avro {
  val carMetricKeySchema: Schema = AvroSchema[CarMetricKey]
  val carMetricValueSchema: Schema = AvroSchema[CarMetricValue]

  val carMetricKeyRecordFormat: RecordFormat[CarMetricKey] = RecordFormat[CarMetricKey]
  val carMetricValueRecordFormat: RecordFormat[CarMetricValue] = RecordFormat[CarMetricValue]

  val carLocationKeySchema: Schema = AvroSchema[CarLocationKey]
  val carLocationValueSchema: Schema = AvroSchema[CarLocationValue]

  val carLocationKeyRecordFormat: RecordFormat[CarLocationKey] = RecordFormat[CarLocationKey]
  val carLocationValueRecordFormat: RecordFormat[CarLocationValue] = RecordFormat[CarLocationValue]

  val weatherKeySchema: Schema = AvroSchema[WeatherKey]
  val weatherValueSchema: Schema = AvroSchema[WeatherValue]

  val weatherKeyRecordFormat: RecordFormat[WeatherKey] = RecordFormat[WeatherKey]
  val weatherValueRecordFormat: RecordFormat[WeatherValue] = RecordFormat[WeatherValue]

  val driverNotificationKeySchema: Schema = AvroSchema[DriverNotificationKey]
  val driverNotificationValueSchema: Schema = AvroSchema[DriverNotificationValue]

  val driverNotificationKeyRecordFormat: RecordFormat[DriverNotificationKey] = RecordFormat[DriverNotificationKey]
  val driverNotificationValueRecordFormat: RecordFormat[DriverNotificationValue] = RecordFormat[DriverNotificationValue]
}
