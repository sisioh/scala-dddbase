package org.sisioh.dddbase.core.lifecycle.forwarding.sync

import org.sisioh.dddbase.core.lifecycle.sync.{SyncRepository, SyncEntityWriter, SyncEntityReader}
import org.sisioh.dddbase.core.model.{Entity, Identity}

/**
 * [[org.sisioh.dddbase.core.lifecycle.sync.SyncRepository]]のデコレータ。
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait ForwardingSyncRepository[ID <: Identity[_], E <: Entity[ID]]
  extends ForwardingSyncEntityReader[ID, E]
  with ForwardingSyncEntityWriter[ID, E]
  with SyncRepository[ID, E] {

  type This <: ForwardingSyncRepository[ID, E]

  type Delegate <: SyncRepository[ID, E]

  /**
   * デリゲート。
   */
  protected val delegate: Delegate

}
