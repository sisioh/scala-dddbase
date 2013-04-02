package org.sisioh.dddbase.core

import java.util.UUID
import org.junit.runner.RunWith
import org.specs2.mutable._
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class EntityCloneableSpec extends Specification {
  val id = Identity(UUID.randomUUID)

  "a cloned entity" should {
    val entity = new Entity[UUID] with EntityCloneable[UUID, Entity[UUID]] {
      val identity = id
    }
    "equal the entity that before clone it" in {
      entity must_== entity.clone
    }
  }
}
