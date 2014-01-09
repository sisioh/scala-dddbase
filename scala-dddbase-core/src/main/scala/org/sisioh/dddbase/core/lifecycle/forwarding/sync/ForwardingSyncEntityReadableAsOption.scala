package org.sisioh.dddbase.core.lifecycle.forwarding.sync

import org.sisioh.dddbase.core.model.{Entity, Identifier}
import scala.util.Try
import org.sisioh.dddbase.core.lifecycle.sync.{SyncEntityReadableAsOption, SyncEntityReader}
import org.sisioh.dddbase.core.lifecycle.EntityIOContext

/**
 * [[org.sisioh.dddbase.core.lifecycle.sync.SyncEntityReadableAsOption]]のデコレータ。
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait ForwardingSyncEntityReadableAsOption[ID <: Identifier[_], E <: Entity[ID]]
  extends SyncEntityReadableAsOption[ID, E] {
  this: SyncEntityReader[ID, E] =>

  type Delegate <: SyncEntityReadableAsOption[ID, E]

  /**
   * デリゲート。
   */
  protected val delegate: Delegate

  def resolveAsOptionBy(identifier: ID)
                   (implicit ctx: EntityIOContext[Try]): Option[E] = delegate.resolveAsOptionBy(identifier)

}
