package iot.avro

import io.circe.generic.auto._
import sttp.client3.circe._
import sttp.client3.{HttpURLConnectionBackend, _}

object RegisterAvroSchemas extends App {

  case class RegisterSchemaRequest(schema: String)

  val backend = HttpURLConnectionBackend()

  Seq(
    ("measurements-key", RegisterSchemaRequest(Avro.measurementKeySchema.toString)),
    ("measurements-value", RegisterSchemaRequest(Avro.measurementValueSchema.toString))
  ).map {
    case (subject, schema) =>
      basicRequest
        .post(uri"http://schema-registry:8081/subjects/$subject/versions")
        .contentType("application/vnd.schemaregistry.v1+json")
        .body(schema)
        .send(backend)
        .code
  } foreach { statusCode => println(s"Registered schema code: $statusCode") }
}
