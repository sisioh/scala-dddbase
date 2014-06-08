package org.sisioh.dddbase.core.model

import java.io._
import java.util.UUID
import org.specs2.mutable._

case class TestSerializableId(value: UUID) extends Identifier[UUID]

// with IdentitySerializable[UUID]

class TestSerializableEntity(val identifier: TestSerializableId, val name: String)
  extends Entity[TestSerializableId] with EntitySerializable[TestSerializableId, TestSerializableEntity]

class EntitySpec extends Specification {

  class TestEntity(val identifier: Identifier[UUID]) extends Entity[Identifier[UUID]]

  val identifier = Identifier(UUID.randomUUID)

  "Entity's Identity" should {
    val entity = new TestEntity(identifier)
    "equal the identifier of the paramter for the constructor" in {
      entity.identifier must_== identifier
    }
  }

  "Entity's equals method return value" should {
    "is true when each entities have a same identifier" in {
      new TestEntity(identifier) must_== new TestEntity(identifier)
    }
    "is false when each entities have a different identifier" in {
      new TestEntity(identifier) must_!= new TestEntity(Identifier(UUID.randomUUID))
    }
  }

  "Entity's hashCode method return value" should {
    "is same when each entities have a same identifier" in {
      new TestEntity(identifier).hashCode must_== new TestEntity(identifier).hashCode
    }
    "is different when each entities have a different identifier" in {
      new TestEntity(identifier).hashCode must_!= new TestEntity(Identifier(UUID.randomUUID)).hashCode
    }
  }

  "not EntitySerializable entity" should {
    "fail to seriarize entity not extends EntitySerializable" in {
      val oos = new ObjectOutputStream(new ByteArrayOutputStream)
      val entity = new TestEntity(identifier)
      oos.writeObject(entity) must throwA[NotSerializableException]
    }
  }

  "EntitySerializable" should {
    "deserialize seriarized entity if entity is serilizable" in {
      val os = new ByteArrayOutputStream()
      val oos = new ObjectOutputStream(os)
      val name = "custom"
      val id = TestSerializableId(UUID.randomUUID)
      val entity = new TestSerializableEntity(id, name)
      oos.writeObject(entity)
      val ois = new ObjectInputStream(new ByteArrayInputStream(os.toByteArray))
      val deserializedEntity: TestSerializableEntity = ois.readObject.asInstanceOf[TestSerializableEntity]
      entity must_== deserializedEntity
      entity.name must_== deserializedEntity.name
    }
  }

}
