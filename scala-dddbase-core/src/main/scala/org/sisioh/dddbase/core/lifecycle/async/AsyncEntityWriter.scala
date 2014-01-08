package org.sisioh.dddbase.core.lifecycle.async

import org.sisioh.dddbase.core.lifecycle.EntityWriter
import org.sisioh.dddbase.core.model.{Entity, Identifier}
import scala.concurrent._

/**
 * 非同期版[[org.sisioh.dddbase.core.lifecycle.EntityWriter]]。
 *
 * @see [[org.sisioh.dddbase.core.lifecycle.EntityWriter]]
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait AsyncEntityWriter[ID <: Identifier[_], E <: Entity[ID]]
  extends AsyncEntityIO with EntityWriter[ID, E, Future] {

  type This <: AsyncEntityWriter[ID, E]
  type Result = AsyncResultWithEntity[This, ID, E]
  type Results = AsyncResultWithEntities[This, ID, E]

  protected final def traverseWithThis[A](values: Seq[A])(processor: (This, A) => Future[Result])(implicit ctx: Ctx): Future[Results] = {
    implicit val executor = getExecutionContext(ctx)
    values.foldLeft(Future.successful(AsyncResultWithEntities[This, ID, E](this.asInstanceOf[This], Seq.empty[E]))) {
      case (future, value) =>
        for {
          AsyncResultWithEntities(repo, entities) <- future
          AsyncResultWithEntity(r, e) <- processor(repo, value)
        } yield {
          AsyncResultWithEntities(r, entities :+ e)
        }
    }
  }

  def multiStore(entities: E*)(implicit ctx: Ctx): Future[Results] =
    traverseWithThis(entities) {
      (repository, entity) =>
        repository.multiStore(entity).asInstanceOf[Future[Result]]
    }

  def multiDeleteBy(identifiers: ID*)(implicit ctx: Ctx): Future[Results] =
    traverseWithThis(identifiers) {
      (repository, identifier) =>
        repository.multiDeleteBy(identifier).asInstanceOf[Future[Result]]
    }

}
