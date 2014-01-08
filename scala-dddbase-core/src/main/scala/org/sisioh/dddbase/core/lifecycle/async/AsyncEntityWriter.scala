package org.sisioh.dddbase.core.lifecycle.async

import org.sisioh.dddbase.core.lifecycle.{ResultWithEntities, EntityIOContext, EntityWriter}
import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.concurrent._
import scala.collection.mutable.ListBuffer
import org.sisioh.dddbase.core.lifecycle.sync.{SyncResultWithEntities, SyncResultWithEntity}

/**
 * 非同期版[[org.sisioh.dddbase.core.lifecycle.EntityWriter]]。
 *
 * @see [[org.sisioh.dddbase.core.lifecycle.EntityWriter]]
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait AsyncEntityWriter[ID <: Identity[_], E <: Entity[ID]]
  extends AsyncEntityIO with EntityWriter[ID, E, Future] {

  type This <: AsyncEntityWriter[ID, E]
  type Result = AsyncResultWithEntity[This, ID, E]
  type Results = AsyncResultWithEntities[This, ID, E]

  def storeEntities(entities: Seq[E])(implicit ctx: Ctx): Future[Results] =
    traverseWithThis(entities) {
      (repository, entity) =>
        repository.storeEntity(entity).asInstanceOf[Future[Result]]
    }

  def deleteByIdentifiers(identifiers: Seq[ID])(implicit ctx: Ctx): Future[Results] =
    traverseWithThis(identifiers) {
      (repository, identifier) =>
        repository.deleteByIdentifier(identifier).asInstanceOf[Future[Result]]
    }

}
