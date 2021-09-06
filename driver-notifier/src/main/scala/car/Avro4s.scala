package car

import com.sksamuel.avro4s.RecordFormat
import io.confluent.kafka.streams.serdes.avro.GenericAvroSerde
import org.apache.avro.generic.GenericRecord
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.streams.kstream.{Consumed, Produced}
import org.apache.kafka.streams.scala.serialization.Serdes

import scala.jdk.CollectionConverters._

object Avro4s {

  implicit class CaseClassSerde(inner: Serde[GenericRecord]) {
    def forCaseClass[T >: Null](implicit rf: RecordFormat[T]): Serde[T] = {
      Serdes.fromFn(
        (topic, data) => inner.serializer().serialize(topic, rf.to(data)),
        (topic, bytes) => Option(rf.from(inner.deserializer().deserialize(topic, bytes)))
      )
    }
  }

  implicit def consumed[K >: Null, V >: Null](props: Map[String, String])(implicit krf: RecordFormat[K], vrf: RecordFormat[V]): Consumed[K, V] = {
    val propsJ = props.asJava

    val avroKeySerde = new GenericAvroSerde
    avroKeySerde.configure(propsJ, true)

    val avroValueSerde = new GenericAvroSerde
    avroValueSerde.configure(propsJ, false)

    Consumed.`with`(avroKeySerde.forCaseClass[K], avroValueSerde.forCaseClass[V])
  }

  implicit def produced[K >: Null, V >: Null](props: Map[String, String])(implicit krf: RecordFormat[K], vrf: RecordFormat[V]): Produced[K, V] = {
    val propsJ = props.asJava

    val avroKeySerde = new GenericAvroSerde
    avroKeySerde.configure(propsJ, true)

    val avroValueSerde = new GenericAvroSerde
    avroValueSerde.configure(propsJ, false)

    Produced.`with`(avroKeySerde.forCaseClass[K], avroValueSerde.forCaseClass[V])
  }
}
