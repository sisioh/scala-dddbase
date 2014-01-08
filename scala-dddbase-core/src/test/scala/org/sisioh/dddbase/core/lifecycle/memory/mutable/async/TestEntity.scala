package org.sisioh.dddbase.core.lifecycle.memory.mutable.async

import org.sisioh.dddbase.core.model.{Identifier, EntityCloneable, Entity}
import java.util.UUID

trait TestEntity
  extends Entity[Identifier[UUID]]
  with EntityCloneable[Identifier[UUID], TestEntity]
  with Ordered[TestEntity] {

}
