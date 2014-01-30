package org.sisioh.dddbase.core.lifecycle.memory.mutable.sync

import org.sisioh.dddbase.core.lifecycle.EntityNotFoundException
import org.sisioh.dddbase.core.lifecycle.sync.SyncResultWithEntity
import org.sisioh.dddbase.core.model.{EntityCloneable, Entity, Identifier}
import scala.util.{Success, Failure, Try}
import scala.collection.Map

trait SyncRepositoryOnMemorySupport
[ID <: Identifier[_], E <: Entity[ID] with EntityCloneable[ID, E] with Ordered[E]]
  extends SyncRepositoryOnMemory[ID, E] {

  private val _entities: collection.mutable.Map[ID, E]  = collection.mutable.Map.empty

  override protected def getEntities: Map[ID, E] = _entities

  override def equals(obj: Any) = obj match {
    case that: SyncRepositoryOnMemorySupport[_, _] =>
      this.entities == that.entities
    case _ => false
  }

  override def hashCode = 31 * entities.##

  override def resolveBy(identifier: ID)(implicit ctx: Ctx) = synchronized {
    existBy(identifier).flatMap {
      _ =>
        Try {
          entities(identifier).clone
        }.recoverWith {
          case ex: NoSuchElementException =>
            Failure(new EntityNotFoundException(Some(s"identifier = $identifier")))
        }
    }
  }

  override def store(entity: E)(implicit ctx: Ctx): Try[Result] = synchronized {
    _entities += (entity.identifier -> entity)
    Success(SyncResultWithEntity(this.asInstanceOf[This], entity))
  }

  override def deleteBy(identifier: ID)(implicit ctx: Ctx): Try[Result] = synchronized {
    resolveBy(identifier).flatMap {
      entity =>
        _entities -= identifier
        Success(SyncResultWithEntity(this.asInstanceOf[This], entity))
    }
  }

  def iterator =
    entities.map(_._2.clone).toSeq.sorted.iterator

}

