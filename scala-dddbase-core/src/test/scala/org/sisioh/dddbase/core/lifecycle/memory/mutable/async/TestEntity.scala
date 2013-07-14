package org.sisioh.dddbase.core.lifecycle.memory.mutable.async

import org.sisioh.dddbase.core.model.{Identity, EntityCloneable, Entity}
import java.util.UUID

trait TestEntity
  extends Entity[Identity[UUID]]
  with EntityCloneable[Identity[UUID], TestEntity]
  with Ordered[TestEntity] {

}
