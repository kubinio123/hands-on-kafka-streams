package car.avro

import io.circe.generic.auto._
import sttp.client3.circe._
import sttp.client3.{HttpURLConnectionBackend, _}

object RegisterAvroSchemas extends App {

  case class RegisterSchemaRequest(schema: String)

  val backend = HttpURLConnectionBackend()

  Seq(
    ("car-metrics-key", RegisterSchemaRequest(Avro.carMetricKeySchema.toString())),
    ("car-metrics-value", RegisterSchemaRequest(Avro.carMetricValueSchema.toString())),
    //
    ("car-locations-key", RegisterSchemaRequest(Avro.carLocationKeySchema.toString())),
    ("car-locations-value", RegisterSchemaRequest(Avro.carLocationValueSchema.toString())),
    //
    ("weather-key", RegisterSchemaRequest(Avro.weatherKeySchema.toString())),
    ("weather-value", RegisterSchemaRequest(Avro.weatherValueSchema.toString())),
    //
    ("driver-notifications-key", RegisterSchemaRequest(Avro.driverNotificationKeySchema.toString())),
    ("driver-notifications-value", RegisterSchemaRequest(Avro.driverNotificationValueSchema.toString()))
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
