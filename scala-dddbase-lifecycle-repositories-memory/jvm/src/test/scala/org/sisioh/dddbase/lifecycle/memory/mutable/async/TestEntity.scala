package org.sisioh.dddbase.lifecycle.memory.mutable.async

import java.util.UUID
import org.sisioh.dddbase.core.model.{ Identifier, EntityCloneable, Entity }

trait TestEntity
    extends Entity[Identifier[UUID]]
    with EntityCloneable[Identifier[UUID], TestEntity]
    with Ordered[TestEntity] {

}
