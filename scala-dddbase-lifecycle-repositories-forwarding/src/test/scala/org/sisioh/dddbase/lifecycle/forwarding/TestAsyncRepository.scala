package org.sisioh.dddbase.lifecycle.forwarding

import org.sisioh.dddbase.core.lifecycle.EntityNotFoundException
import org.sisioh.dddbase.core.lifecycle.async.{ AsyncResultWithEntity, AsyncRepository }
import org.sisioh.dddbase.core.model.{ Entity, Identifier }
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

case class TestAsyncRepository[ID <: Identifier[_], E <: Entity[ID]](entities: Map[ID, E] = Map.empty[ID, E]) extends AsyncRepository[ID, E] {

  override type This = TestAsyncRepository[ID, E]

  override def existBy(identifier: ID)(implicit ctx: Ctx): Future[Boolean] = Future {
    entities.contains(identifier)
  }

  override def resolveBy(identifier: ID)(implicit ctx: Ctx): Future[E] = Future {
    entities.get(identifier).getOrElse(throw EntityNotFoundException())
  }

  override def store(entity: E)(implicit ctx: Ctx): Future[Result] = Future {
    AsyncResultWithEntity[This, ID, E](TestAsyncRepository(entities + (entity.identifier -> entity)).asInstanceOf[This], entity)
  }

  override def deleteBy(identifier: ID)(implicit ctx: Ctx): Future[Result] = Future {
    val entity = entities.get(identifier).getOrElse(throw EntityNotFoundException())
    AsyncResultWithEntity[This, ID, E](TestAsyncRepository(entities - identifier).asInstanceOf[This], entity)
  }
}
