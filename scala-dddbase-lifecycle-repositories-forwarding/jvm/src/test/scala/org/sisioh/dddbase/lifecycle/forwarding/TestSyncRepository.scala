package org.sisioh.dddbase.lifecycle.forwarding

import org.sisioh.dddbase.core.lifecycle.sync.{ SyncResultWithEntity, SyncRepository }
import org.sisioh.dddbase.core.model.{ Entity, Identifier }
import scala.util.Try
import org.sisioh.dddbase.core.lifecycle.EntityNotFoundException

case class TestSyncRepository[ID <: Identifier[_], E <: Entity[ID]](entities: Map[ID, E] = Map.empty[ID, E]) extends SyncRepository[ID, E] {

  override type This = TestSyncRepository[ID, E]

  override def existBy(identifier: ID)(implicit ctx: Ctx): Try[Boolean] = Try {
    entities.contains(identifier)
  }

  override def resolveBy(identifier: ID)(implicit ctx: Ctx): Try[E] = Try {
    entities.get(identifier).getOrElse(throw EntityNotFoundException())
  }

  override def store(entity: E)(implicit ctx: Ctx): Try[Result] = Try {
    SyncResultWithEntity[This, ID, E](TestSyncRepository(entities + (entity.identifier -> entity)).asInstanceOf[This], entity)
  }

  override def deleteBy(identifier: ID)(implicit ctx: Ctx): Try[Result] = Try {
    val entity = entities.get(identifier).getOrElse(throw EntityNotFoundException())
    SyncResultWithEntity[This, ID, E](TestSyncRepository(entities - identifier).asInstanceOf[This], entity)
  }
}
