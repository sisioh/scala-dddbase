package org.sisioh.dddbase.core.model

import java.io._
import org.specs2.mutable._

class DummyValue extends Ordered[DummyValue]{
  def compare(that: DummyValue): Int = 0
}

class DummySerializableValue extends Serializable with Ordered[DummySerializableValue]{
  def compare(that: DummySerializableValue): Int = 0
}

case class NotSerializableIdentifier(value: DummyValue) extends Identifier[DummyValue] //with IdentitySerializable[DummyValue]

case class SerializableIdentifier(value: Long) extends Identifier[Long] // with IdentitySerializable[Long]

class IdentitySpec extends Specification {
  "Identity" should {

    "throw EmptyIdentityException when get value" in {
      val identity: Identifier[Long] = Identifier.empty
      identity.value must throwA[EmptyIdentityException]
    }

    "throw NotSerializableExcepption when serialization not serializable identity" in {
      val identity = Identifier(new DummyValue)
      val oos = new ObjectOutputStream(new ByteArrayOutputStream)
      oos.writeObject(identity) must throwA[NotSerializableException]
    }

    "deserialize serialized serializable identity" in {
      val identity = Identifier(new DummySerializableValue)
      val os = new ByteArrayOutputStream
      val oos = new ObjectOutputStream(os)
      oos.writeObject(identity)
      val ois = new ObjectInputStream(new ByteArrayInputStream(os.toByteArray))
      val deserializedIdentity = ois.readObject
      deserializedIdentity must beAnInstanceOf[Identifier[DummySerializableValue]]
    }

    "throw NotSerializableExcepption when serialization the identity which have one or more not serializable member" in {
      val identity = NotSerializableIdentifier(new DummyValue)
      val oos = new ObjectOutputStream(new ByteArrayOutputStream)
      oos.writeObject(identity) must throwA[NotSerializableException]
    }

    "deserialize serialized serializable identity contains primary value" in {
      val identity = SerializableIdentifier(100L)
      val os = new ByteArrayOutputStream
      val oos = new ObjectOutputStream(os)
      oos.writeObject(identity)
      val ois = new ObjectInputStream(new ByteArrayInputStream(os.toByteArray))
      val deserializedIdentity = ois.readObject.asInstanceOf[SerializableIdentifier]
      identity must_== deserializedIdentity
    }

  }
}
