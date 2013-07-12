package org.sisioh.dddbase.core.lifecycle.forwarding.async

import org.sisioh.dddbase.core.lifecycle.EntitiesChunk
import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.concurrent.{Future, ExecutionContext}
import org.sisioh.dddbase.core.lifecycle.async.{AsyncEntityReaderByPredicate, AsyncEntityReader}

trait ForwardingAsyncEntityReaderByPredicate[ID <: Identity[_], T <: Entity[ID]]
  extends AsyncEntityReaderByPredicate[ID, T] {
  this: AsyncEntityReader[ID, T] =>

  protected val delegateAsyncEntityReaderByPredicate: AsyncEntityReaderByPredicate[ID, T]

  def filterByPredicate(predicate: (T) => Boolean, index: Option[Int], maxEntities: Option[Int]): Future[EntitiesChunk[ID, T]] =
    delegateAsyncEntityReaderByPredicate.filterByPredicate(predicate, index, maxEntities)

}
