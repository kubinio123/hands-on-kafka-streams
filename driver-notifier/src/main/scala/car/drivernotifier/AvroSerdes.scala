package car.drivernotifier

import car.avro.{KeyRecordFormat, ValueRecordFormat}
import com.sksamuel.avro4s.RecordFormat
import io.confluent.kafka.streams.serdes.avro.GenericAvroSerde
import org.apache.avro.generic.GenericRecord
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.streams.scala.serialization.Serdes

import scala.jdk.CollectionConverters._

object AvroSerdes {

  private val props = Map("schema.registry.url" -> "http://schema-registry:8081")

  implicit def keySerde[K >: Null](implicit krf: KeyRecordFormat[K]): Serde[K] = {
    val avroKeySerde = new GenericAvroSerde
    avroKeySerde.configure(props.asJava, true)
    avroKeySerde.forCaseClass[K]
  }

  implicit def valueSerde[V >: Null](implicit vrf: ValueRecordFormat[V]): Serde[V] = {
    val avroValueSerde = new GenericAvroSerde
    avroValueSerde.configure(props.asJava, false)
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
