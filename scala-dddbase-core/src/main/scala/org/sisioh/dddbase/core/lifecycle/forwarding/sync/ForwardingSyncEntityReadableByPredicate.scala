package org.sisioh.dddbase.core.lifecycle.forwarding.sync

import org.sisioh.dddbase.core.lifecycle.{EntityIOContext, EntitiesChunk}
import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.util.Try
import org.sisioh.dddbase.core.lifecycle.sync.{SyncEntityReadableByPredicate, SyncEntityReader}

/**
 * [[org.sisioh.dddbase.core.lifecycle.sync.SyncEntityReadableByPredicate]]のデコレータ。
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait ForwardingSyncEntityReadableByPredicate[CTX <: EntityIOContext[Try], ID <: Identity[_], E <: Entity[ID]]
  extends SyncEntityReadableByPredicate[CTX, ID, E] {
  this: SyncEntityReader[CTX, ID, E] =>

  type Delegate <: SyncEntityReadableByPredicate[CTX, ID, E]

  /**
   * デリゲート。
   */
  protected val delegate: Delegate

  def filterByPredicate
  (predicate: (E) => Boolean,
   index: Option[Int], maxEntities: Option[Int])
  (implicit ctx: CTX): Try[EntitiesChunk[ID, E]] =
    delegate.filterByPredicate(predicate, index, maxEntities)

}
