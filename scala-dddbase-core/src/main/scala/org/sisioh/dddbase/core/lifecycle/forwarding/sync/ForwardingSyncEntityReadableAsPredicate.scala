package org.sisioh.dddbase.core.lifecycle.forwarding.sync

import org.sisioh.dddbase.core.lifecycle.{EntityIOContext, EntitiesChunk}
import org.sisioh.dddbase.core.model.{Entity, Identifier}
import scala.util.Try
import org.sisioh.dddbase.core.lifecycle.sync.{SyncEntityReadableAsPredicate, SyncEntityReader}

/**
 * [[org.sisioh.dddbase.core.lifecycle.sync.SyncEntityReadableAsPredicate]]のデコレータ。
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait ForwardingSyncEntityReadableAsPredicate[ID <: Identifier[_], E <: Entity[ID]]
  extends SyncEntityReadableAsPredicate[ID, E] {
  this: SyncEntityReader[ID, E] =>

  type Delegate <: SyncEntityReadableAsPredicate[ID, E]

  /**
   * デリゲート。
   */
  protected val delegate: Delegate

  def filterBy
  (predicate: (E) => Boolean,
   index: Option[Int], maxEntities: Option[Int])
  (implicit ctx: EntityIOContext[Try]): Try[EntitiesChunk[ID, E]] =
    delegate.filterBy(predicate, index, maxEntities)

}
