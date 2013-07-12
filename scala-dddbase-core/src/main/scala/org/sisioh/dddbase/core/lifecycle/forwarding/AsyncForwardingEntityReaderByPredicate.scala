package org.sisioh.dddbase.core.lifecycle.forwarding

import org.sisioh.dddbase.core.lifecycle.{EntitiesChunk, AsyncEntityReader, AsyncEntityReaderByPredicate}
import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.concurrent.{Future, ExecutionContext}

trait AsyncForwardingEntityReaderByPredicate[ID <: Identity[_], T <: Entity[ID]]
  extends AsyncEntityReaderByPredicate[ID, T] {
  this: AsyncEntityReader[ID, T] =>

  protected val delegateAsyncEntityReaderByPredicate: AsyncEntityReaderByPredicate[ID, T]

  def filterByPredicate(predicate: (T) => Boolean, index: Option[Int], maxEntities: Option[Int]): Future[EntitiesChunk[ID, T]] =
    delegateAsyncEntityReaderByPredicate.filterByPredicate(predicate, index, maxEntities)

}
