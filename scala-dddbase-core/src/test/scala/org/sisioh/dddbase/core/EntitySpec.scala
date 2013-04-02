package org.sisioh.dddbase.core

import java.util.UUID
import org.junit.runner.RunWith
import org.specs2.mutable._
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class EntitySpec extends Specification {

  class TestEntity(val identity: Identity[UUID]) extends Entity[UUID]

  val identity = Identity(UUID.randomUUID)

  "Entity's Identity" should {
    val entity = new TestEntity(identity)
    "equal the identity of the paramter for the constructor" in {
      entity.identity must_== identity
    }
  }

  "Entity's equals method return value" should {
    "is true when each entities have a same identity" in {
      new TestEntity(identity) must_== new TestEntity(identity)
    }
    "is false when each entities have a different identity" in {
      new TestEntity(identity) must_!= new TestEntity(Identity(UUID.randomUUID))
    }
  }

  "Entity's hashCode method return value" should {
    "is same when each entities have a same identity" in {
      new TestEntity(identity).hashCode must_== new TestEntity(identity).hashCode
    }
    "is different when each entities have a different identity" in {
      new TestEntity(identity).hashCode must_!= new TestEntity(Identity(UUID.randomUUID)).hashCode
    }
  }

}
