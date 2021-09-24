package car

import car.avro.Avro.{KeyRecordFormat, ValueRecordFormat}
import com.sksamuel.avro4s.RecordFormat
import io.confluent.kafka.streams.serdes.avro.GenericAvroSerde
import org.apache.avro.generic.GenericRecord
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.streams.scala.kstream.{Consumed, Grouped, Produced}
import org.apache.kafka.streams.scala.serialization.Serdes

import scala.jdk.CollectionConverters._

object Avro4s {

//  case class SchemaRegistryUrl(value: String)

//  implicit def consumed[K >: Null, V >: Null](implicit kSerde: Serde[K], vSerde: Serde[V]): Consumed[K, V] =
//    Consumed.`with`[K, V]
//
//  implicit def produced[K >: Null, V >: Null](implicit kSerde: Serde[K], vSerde: Serde[V]): Produced[K, V] =
//    Produced.`with`[K, V]
//
//  implicit def grouped[K >: Null, V >: Null](implicit kSerde: Serde[K], vSerde: Serde[V]): Grouped[K, V] =
//    Grouped.`with`[K, V]

  implicit def keySerde[K >: Null](implicit krf: KeyRecordFormat[K]): Serde[K] = {
    val avroKeySerde = new GenericAvroSerde
    avroKeySerde.configure(Map("schema.registry.url" -> "http://schema-registry:8081").asJava, true)
    avroKeySerde.forCaseClass[K]
  }

  implicit def valueSerde[V >: Null](implicit vrf: ValueRecordFormat[V]): Serde[V] = {
    val avroValueSerde = new GenericAvroSerde
    avroValueSerde.configure(Map("schema.registry.url" -> "http://schema-registry:8081").asJava, false)
    avroValueSerde.forCaseClass[V]
  }

  implicit class CaseClassSerde(inner: Serde[GenericRecord]) {
    def forCaseClass[T >: Null](implicit rf: RecordFormat[T]): Serde[T] = {
      Serdes.fromFn(
        (topic, data) => inner.serializer().serialize(topic, rf.to(data)),
        (topic, bytes) => Option(rf.from(inner.deserializer().deserialize(topic, bytes)))
      )
    }
  }
}
