package org.sisioh.dddbase.core.lifecycle.forwarding.sync

import org.sisioh.dddbase.core.lifecycle.{EntityIOContext, EntitiesChunk}
import org.sisioh.dddbase.core.lifecycle.sync.{SyncEntityReadableByChunk, SyncEntityReader}
import org.sisioh.dddbase.core.model.{Identity, Entity}
import scala.util.Try

/**
 * [[org.sisioh.dddbase.core.lifecycle.sync.SyncEntityReadableByChunk]]のデコレータ。
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait ForwardingSyncEntityReadableByChunk[CTX <: EntityIOContext[Try], ID <: Identity[_], E <: Entity[ID]]
  extends SyncEntityReadableByChunk[CTX, ID, E] {
  this: SyncEntityReader[CTX, ID, E] =>

  type Delegate <: SyncEntityReadableByChunk[CTX, ID, E]

  /**
   * デリゲート。
   */
  protected val delegate: Delegate

  def resolveChunk(index: Int, maxEntities: Int)
                  (implicit ctx: CTX): Try[EntitiesChunk[ID, E]] =
    delegate.resolveChunk(index, maxEntities)

}


