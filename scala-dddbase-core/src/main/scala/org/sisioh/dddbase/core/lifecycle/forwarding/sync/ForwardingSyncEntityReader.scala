package org.sisioh.dddbase.core.lifecycle.forwarding.sync

import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.util.Try
import org.sisioh.dddbase.core.lifecycle.sync.SyncEntityReader

/**
 * [[org.sisioh.dddbase.core.lifecycle.sync.SyncEntityReader]]のデコレータ実装
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait ForwardingSyncEntityReader[ID <: Identity[_], E <: Entity[ID]]
  extends SyncEntityReader[ID, E] {

  /**
   * デリゲート。
   */
  protected val delegateEntityReader: SyncEntityReader[ID, E]

  def resolve(identity: ID): Try[E] = delegateEntityReader.resolve(identity)

  def contains(identity: ID): Try[Boolean] = delegateEntityReader.contains(identity)

}
