package org.sisioh.dddbase.core.lifecycle.forwarding.sync

import org.sisioh.dddbase.core.lifecycle.EntitiesChunk
import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.util.Try
import org.sisioh.dddbase.core.lifecycle.sync.{SyncEntityReaderByPredicate, SyncEntityReader}

trait ForwardingSyncEntityReaderByPredicate[ID <: Identity[_], T <: Entity[ID]]
  extends SyncEntityReaderByPredicate[ID, T] {
  this: SyncEntityReader[ID, T] =>

  protected val delegateEntityReaderByPredicate: SyncEntityReaderByPredicate[ID, T]

  def filterByPredicate(predicate: (T) => Boolean, index: Option[Int], maxEntities: Option[Int]): Try[EntitiesChunk[ID, T]] =
    delegateEntityReaderByPredicate.filterByPredicate(predicate, index, maxEntities)

}
