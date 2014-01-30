package org.sisioh.dddbase.core.lifecycle.memory.mutable.async

import org.sisioh.dddbase.core.model.{EntityCloneable, Entity, Identifier}
import scala.concurrent._
import org.sisioh.dddbase.core.lifecycle.EntityNotFoundException
import org.sisioh.dddbase.core.lifecycle.async.AsyncResultWithEntity
import scala.util.Success
import org.sisioh.dddbase.core.lifecycle.sync.SyncResultWithEntity
import scala.collection.Map

trait AsyncRepositoryOnMemorySupport
[ID <: Identifier[_], E <: Entity[ID] with EntityCloneable[ID, E] with Ordered[E]]
extends AsyncRepositoryOnMemory[ID, E] {

  private val _entities = collection.mutable.Map.empty[ID, E]

  override protected def getEntities: Map[ID, E] = _entities

  override def existBy(identifier: ID)(implicit ctx: Ctx): Future[Boolean] =
    Future.successful(getEntities.contains(identifier))

  override def resolveBy(identifier: ID)(implicit ctx: Ctx): Future[E] = {
    implicit val executor = getExecutionContext(ctx)
    future {
      _entities.get(identifier).getOrElse(throw new EntityNotFoundException(Some(s"identifier = $identifier")))
    }
  }

  override def store(entity: E)(implicit ctx: Ctx): Future[Result] = {
    _entities += (entity.identifier -> entity)
    Future.successful(AsyncResultWithEntity[This, ID, E](this.asInstanceOf[This], entity))
  }

  override def deleteBy(identifier: ID)(implicit ctx: Ctx): Future[Result] = {
    implicit val executor = getExecutionContext(ctx)
    resolveBy(identifier).flatMap {
      entity =>
        _entities -= identifier
        Future.successful(AsyncResultWithEntity[This, ID, E](this.asInstanceOf[This], entity))
    }
  }

}
