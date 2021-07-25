package iot.avro

import com.sksamuel.avro4s.{AvroSchema, RecordFormat}
import iot.{MeasurementKey, MeasurementValue}
import org.apache.avro.Schema

object Avro {
  val measurementKeySchema: Schema = AvroSchema[MeasurementKey]
  val measurementValueSchema: Schema = AvroSchema[MeasurementValue]

  val measurementKeyRecordFormat: RecordFormat[MeasurementKey] = RecordFormat[MeasurementKey]
  val measurementValueRecordFormat: RecordFormat[MeasurementValue] = RecordFormat[MeasurementValue]
}
