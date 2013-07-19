package org.sisioh.dddbase.core.lifecycle.forwarding.sync

import org.sisioh.dddbase.core.lifecycle.EntitiesChunk
import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.util.Try
import org.sisioh.dddbase.core.lifecycle.sync.{SyncEntityReadableByPredicate, SyncEntityReader}

/**
 * [[org.sisioh.dddbase.core.lifecycle.sync.SyncEntityReadableByPredicate]]のデコレータ。
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait ForwardingSyncEntityReadableByPredicate[ID <: Identity[_], E <: Entity[ID]]
  extends SyncEntityReadableByPredicate[ID, E] {
  this: SyncEntityReader[ID, E] =>

  /**
   * デリゲート。
   */
  protected val delegateEntityReaderByPredicate: SyncEntityReadableByPredicate[ID, E]

  def filterByPredicate
  (predicate: (E) => Boolean,
   index: Option[Int], maxEntities: Option[Int]): Try[EntitiesChunk[ID, E]] =
    delegateEntityReaderByPredicate.filterByPredicate(predicate, index, maxEntities)

}
