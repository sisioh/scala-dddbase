package org.sisioh.dddbase.core.lifecycle.forwarding.sync

import org.sisioh.dddbase.core.lifecycle.sync.{SyncRepository, SyncEntityWriter, SyncEntityReader}
import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.util.Try
import org.sisioh.dddbase.core.lifecycle.EntityIOContext

/**
 * [[org.sisioh.dddbase.core.lifecycle.sync.SyncRepository]]のデコレータ。
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait ForwardingSyncRepository[CTX <: EntityIOContext[Try], ID <: Identity[_], E <: Entity[ID]]
  extends ForwardingSyncEntityReader[CTX, ID, E]
  with ForwardingSyncEntityWriter[CTX, ID, E]
  with SyncRepository[CTX, ID, E] {

  type This <: ForwardingSyncRepository[CTX, ID, E]

  type Delegate <: SyncRepository[CTX, ID, E]

  /**
   * デリゲート。
   */
  protected val delegate: Delegate

}
