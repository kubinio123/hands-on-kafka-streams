package car.avro

import io.circe.generic.auto._
import sttp.client3.circe._
import sttp.client3.{HttpURLConnectionBackend, _}

object RegisterAvroSchemas extends App {

  case class RegisterSchemaRequest(schema: String)

  val backend = HttpURLConnectionBackend()

  Seq(
    ("car-speed-key", RegisterSchemaRequest(Avro.carIdSchema.toString())),
    ("car-speed-value", RegisterSchemaRequest(Avro.carSpeedSchema.toString())),
    //
    ("car-engine-key", RegisterSchemaRequest(Avro.carIdSchema.toString())),
    ("car-engine-value", RegisterSchemaRequest(Avro.carEngineSchema.toString())),
    //
    ("car-location-key", RegisterSchemaRequest(Avro.carIdSchema.toString())),
    ("car-location-value", RegisterSchemaRequest(Avro.carLocationSchema.toString())),
    //
    ("location-data-key", RegisterSchemaRequest(Avro.locationDataKeySchema.toString())),
    ("location-data-value", RegisterSchemaRequest(Avro.locationDataSchema.toString())),
    //
    ("driver-notification-key", RegisterSchemaRequest(Avro.carIdSchema.toString())),
    ("driver-notification-value", RegisterSchemaRequest(Avro.driverNotificationSchema.toString()))
  ).map {
    case (subject, schema) =>
      subject -> basicRequest
        .post(uri"http://schema-registry:8081/subjects/$subject/versions")
        .contentType("application/vnd.schemaregistry.v1+json")
        .body(schema)
        .send(backend)
        .code
  } foreach { case (subject, statusCode) => println(s"Register schema $subject, response code: $statusCode") }
}
