package org.sisioh.dddbase.core.lifecycle.forwarding.sync

import org.sisioh.dddbase.core.lifecycle.EntitiesChunk
import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.util.Try
import org.sisioh.dddbase.core.lifecycle.sync.{SyncEntityReaderByPredicate, SyncEntityReader}

trait ForwardingSyncEntityReaderByPredicate[ID <: Identity[_], E <: Entity[ID]]
  extends SyncEntityReaderByPredicate[ID, E] {
  this: SyncEntityReader[ID, E] =>

  protected val delegateEntityReaderByPredicate: SyncEntityReaderByPredicate[ID, E]

  def filterByPredicate(predicate: (E) => Boolean, index: Option[Int], maxEntities: Option[Int]): Try[EntitiesChunk[ID, E]] =
    delegateEntityReaderByPredicate.filterByPredicate(predicate, index, maxEntities)

}
