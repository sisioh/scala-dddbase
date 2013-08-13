package org.sisioh.dddbase.core.lifecycle.forwarding.sync

import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.util.Try
import org.sisioh.dddbase.core.lifecycle.sync.SyncEntityReader
import org.sisioh.dddbase.core.lifecycle.EntityIOContext

/**
 * [[org.sisioh.dddbase.core.lifecycle.sync.SyncEntityReader]]のデコレータ実装
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait ForwardingSyncEntityReader[ID <: Identity[_], E <: Entity[ID]]
  extends SyncEntityReader[ID, E] {

  type Delegate <: SyncEntityReader[ID, E]

  /**
   * デリゲート。
   */
  protected val delegate: Delegate

  def resolve(identity: ID)(implicit ctx: EntityIOContext[Try]): Try[E] = delegate.resolve(identity)

  def contains(identity: ID)(implicit ctx: EntityIOContext[Try]): Try[Boolean] = delegate.contains(identity)

}
