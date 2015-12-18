package org.sisioh.dddbase.core.model

import java.io._
import org.specs2.mutable._

class DummyValue extends Ordered[DummyValue] {
  def compare(that: DummyValue): Int = 0
}

class DummySerializableValue extends Serializable with Ordered[DummySerializableValue] {
  def compare(that: DummySerializableValue): Int = 0
}

case class NotSerializableIdentifier(value: DummyValue) extends Identifier[DummyValue]

//with IdentitySerializable[DummyValue]

case class SerializableIdentifier(value: Long) extends Identifier[Long]

trait UserId extends Identifier[Long]

case object EmptyUserId extends EmptyIdentifier with UserId

case class UserIdImpl(value: Long) extends UserId

// with IdentitySerializable[Long]]

class IdentitySpec extends Specification {
  "Identity" should {
    def getUserId(n: Int): Identifier[Long] = {
      if (n % 2 == 0)
        new UserIdImpl(1)
      else
        EmptyUserId
    }
    val id: Identifier[Long] = getUserId(1)

    "throw EmptyIdentityException when get value" in {
      val identifier: Identifier[Long] = Identifier.empty
      identifier.value must throwA[EmptyIdentifierException]
    }

    "throw NotSerializableExcepption when serialization not serializable identifier" in {
      val identifier = Identifier(new DummyValue)
      val oos = new ObjectOutputStream(new ByteArrayOutputStream)
      oos.writeObject(identifier) must throwA[NotSerializableException]
    }

    "deserialize serialized serializable identifier" in {
      val identifier = Identifier(new DummySerializableValue)
      val os = new ByteArrayOutputStream
      val oos = new ObjectOutputStream(os)
      oos.writeObject(identifier)
      val ois = new ObjectInputStream(new ByteArrayInputStream(os.toByteArray))
      val deserializedIdentity = ois.readObject
      deserializedIdentity must beAnInstanceOf[Identifier[DummySerializableValue]]
    }

    "throw NotSerializableExcepption when serialization the identifier which have one or more not serializable member" in {
      val identifier = NotSerializableIdentifier(new DummyValue)
      val oos = new ObjectOutputStream(new ByteArrayOutputStream)
      oos.writeObject(identifier) must throwA[NotSerializableException]
    }

    "deserialize serialized serializable identifier contains primary value" in {
      val identifier = SerializableIdentifier(100L)
      val os = new ByteArrayOutputStream
      val oos = new ObjectOutputStream(os)
      oos.writeObject(identifier)
      val ois = new ObjectInputStream(new ByteArrayInputStream(os.toByteArray))
      val deserializedIdentity = ois.readObject.asInstanceOf[SerializableIdentifier]
      identifier must_== deserializedIdentity
    }

  }
}
