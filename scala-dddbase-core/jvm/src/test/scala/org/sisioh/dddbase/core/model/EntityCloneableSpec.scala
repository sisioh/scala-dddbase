package org.sisioh.dddbase.core.model

import java.util.UUID
import org.specs2.mutable._

class EntityCloneableSpec extends Specification {
  val id = Identifier(UUID.randomUUID)

  "a cloned entity" should {
    val entity = new Entity[Identifier[UUID]] with EntityCloneable[Identifier[UUID], Entity[Identifier[UUID]]] {
      val identifier = id
    }
    "equal the entity that before clone it" in {
      entity must_== entity.clone
    }
  }
}
