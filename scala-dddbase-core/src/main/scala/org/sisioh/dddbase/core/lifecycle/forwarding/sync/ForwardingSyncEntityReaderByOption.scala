package org.sisioh.dddbase.core.lifecycle.forwarding.sync

import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.util.Try
import org.sisioh.dddbase.core.lifecycle.sync.{SyncEntityReaderByOption, SyncEntityReader}

/**
 * [[org.sisioh.dddbase.core.lifecycle.sync.SyncEntityReaderByOption]]のデコレータ。
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait ForwardingSyncEntityReaderByOption[ID <: Identity[_], E <: Entity[ID]]
  extends SyncEntityReaderByOption[ID, E] {
  this: SyncEntityReader[ID, E] =>

  /**
   * デリゲート。
   */
  protected val delegateEntityReaderByOption: SyncEntityReaderByOption[ID, E]

  def resolveOption(identity: ID): Try[Option[E]] = delegateEntityReaderByOption.resolveOption(identity)

}
