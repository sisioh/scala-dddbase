package org.sisioh.dddbase.core.lifecycle.forwarding.sync

import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.util.Try
import org.sisioh.dddbase.core.lifecycle.sync.{SyncEntityReadableByOption, SyncEntityReader}
import org.sisioh.dddbase.core.lifecycle.EntityIOContext

/**
 * [[org.sisioh.dddbase.core.lifecycle.sync.SyncEntityReadableByOption]]のデコレータ。
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait ForwardingSyncEntityReadableByOption[ID <: Identity[_], E <: Entity[ID]]
  extends SyncEntityReadableByOption[ID, E] {
  this: SyncEntityReader[ID, E] =>

  type Delegate <: SyncEntityReadableByOption[ID, E]

  /**
   * デリゲート。
   */
  protected val delegate: Delegate

  def resolveOption(identity: ID)
                   (implicit ctx: EntityIOContext[Try]): Try[Option[E]] = delegate.resolveOption(identity)

}
