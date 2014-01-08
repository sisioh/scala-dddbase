package org.sisioh.dddbase.core.lifecycle.forwarding.sync

import org.sisioh.dddbase.core.model.{Entity, Identifier}
import scala.util.Try
import org.sisioh.dddbase.core.lifecycle.sync.SyncEntityReader
import org.sisioh.dddbase.core.lifecycle.EntityIOContext

/**
 * [[org.sisioh.dddbase.core.lifecycle.sync.SyncEntityReader]]のデコレータ実装
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait ForwardingSyncEntityReader[ID <: Identifier[_], E <: Entity[ID]]
  extends SyncEntityReader[ID, E] {

  type Delegate <: SyncEntityReader[ID, E]

  /**
   * デリゲート。
   */
  protected val delegate: Delegate

  def resolveBy(identifier: ID)(implicit ctx: Ctx): Try[E] = delegate.resolveBy(identifier)

  def existBy(identifier: ID)(implicit ctx: Ctx): Try[Boolean] =
    delegate.existBy(identifier)

}
