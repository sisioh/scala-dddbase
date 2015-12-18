package org.sisioh.dddbase.lifecycle.forwarding

import org.sisioh.dddbase.core.lifecycle.EntityNotFoundException
import org.sisioh.dddbase.core.lifecycle.async.{ AsyncResultWithEntity, AsyncRepository }
import org.sisioh.dddbase.core.model.{ Entity, Identifier }
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

case class TestAsyncMutableRepository[ID <: Identifier[_], E <: Entity[ID]]() extends AsyncRepository[ID, E] {

  override type This = TestAsyncMutableRepository[ID, E]

  val entities = collection.mutable.Map.empty[ID, E]

  override def existBy(identifier: ID)(implicit ctx: Ctx): Future[Boolean] = Future {
    entities.contains(identifier)
  }

  override def resolveBy(identifier: ID)(implicit ctx: Ctx): Future[E] = Future {
    entities.get(identifier).getOrElse(throw EntityNotFoundException())
  }

  override def store(entity: E)(implicit ctx: Ctx): Future[Result] = Future {
    entities.put(entity.identifier, entity)
    AsyncResultWithEntity[This, ID, E](this.asInstanceOf[This], entity)
  }

  override def deleteBy(identifier: ID)(implicit ctx: Ctx): Future[Result] = Future {
    val entity = entities.remove(identifier).getOrElse(throw EntityNotFoundException())
    AsyncResultWithEntity[This, ID, E](this.asInstanceOf[This], entity)
  }
}
