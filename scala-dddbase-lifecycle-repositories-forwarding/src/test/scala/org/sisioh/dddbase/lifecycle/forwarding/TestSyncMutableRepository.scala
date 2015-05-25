package org.sisioh.dddbase.lifecycle.forwarding

import org.sisioh.dddbase.core.lifecycle.EntityNotFoundException
import org.sisioh.dddbase.core.lifecycle.sync.{ SyncResultWithEntity, SyncRepository }
import org.sisioh.dddbase.core.model.{ Entity, Identifier }
import scala.util.Try

case class TestSyncMutableRepository[ID <: Identifier[_], E <: Entity[ID]]() extends SyncRepository[ID, E] {
  override type This = TestSyncMutableRepository[ID, E]

  val entities = collection.mutable.Map.empty[ID, E]

  override def existBy(identifier: ID)(implicit ctx: Ctx): Try[Boolean] = Try {
    entities.contains(identifier)
  }

  override def resolveBy(identifier: ID)(implicit ctx: Ctx): Try[E] = Try {
    entities.get(identifier).getOrElse(throw EntityNotFoundException())
  }

  override def store(entity: E)(implicit ctx: Ctx): Try[Result] = Try {
    entities.put(entity.identifier, entity)
    SyncResultWithEntity[This, ID, E](this.asInstanceOf[This], entity)
  }

  override def deleteBy(identifier: ID)(implicit ctx: Ctx): Try[Result] = Try {
    val entity = entities.remove(identifier).getOrElse(throw EntityNotFoundException())
    SyncResultWithEntity[This, ID, E](this.asInstanceOf[This], entity)
  }
}
