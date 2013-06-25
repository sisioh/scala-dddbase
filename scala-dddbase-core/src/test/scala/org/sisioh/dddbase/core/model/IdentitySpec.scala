package org.sisioh.dddbase.core.model

import java.io._
import org.specs2.mutable._

class DummyValue

class DummySerializableValue extends Serializable

case class NotSerializableIdentity(value: DummyValue) extends Identity[DummyValue] //with IdentitySerializable[DummyValue]

case class SerializableIdentity(value: Long) extends Identity[Long] // with IdentitySerializable[Long]

class IdentitySpec extends Specification {
  "Identity" should {

    "throw EmptyIdentityException when get value" in {
      val identity: Identity[Long] = Identity.empty
      identity.value must throwA[EmptyIdentityException]
    }

    "throw NotSerializableExcepption when serialization not serializable identity" in {
      val identity = Identity(new DummyValue)
      val oos = new ObjectOutputStream(new ByteArrayOutputStream)
      oos.writeObject(identity) must throwA[NotSerializableException]
    }

    "deserialize serialized serializable identity" in {
      val identity = Identity(new DummySerializableValue)
      val os = new ByteArrayOutputStream
      val oos = new ObjectOutputStream(os)
      oos.writeObject(identity)
      val ois = new ObjectInputStream(new ByteArrayInputStream(os.toByteArray))
      val deserializedIdentity = ois.readObject
      deserializedIdentity must beAnInstanceOf[Identity[DummySerializableValue]]
    }

    "throw NotSerializableExcepption when serialization the identity which have one or more not serializable member" in {
      val identity = NotSerializableIdentity(new DummyValue)
      val oos = new ObjectOutputStream(new ByteArrayOutputStream)
      oos.writeObject(identity) must throwA[NotSerializableException]
    }

    "deserialize serialized serializable identity contains primary value" in {
      val identity = SerializableIdentity(100L)
      val os = new ByteArrayOutputStream
      val oos = new ObjectOutputStream(os)
      oos.writeObject(identity)
      val ois = new ObjectInputStream(new ByteArrayInputStream(os.toByteArray))
      val deserializedIdentity = ois.readObject.asInstanceOf[SerializableIdentity]
      identity must_== deserializedIdentity
    }

  }
}
