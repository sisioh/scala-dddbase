package org.sisioh.dddbase.core.model

import java.util.UUID
import org.sisioh.dddbase.core.model.{Identity, EntityCloneable, Entity}
import org.specs2.mutable._

class EntityCloneableSpec extends Specification {
  val id = Identity(UUID.randomUUID)

  "a cloned entity" should {
    val entity = new Entity[Identity[UUID]] with EntityCloneable[Identity[UUID], Entity[Identity[UUID]]] {
      val identity = id
    }
    "equal the entity that before clone it" in {
      entity must_== entity.clone
    }
  }
}
