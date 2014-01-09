package org.sisioh.dddbase.core.lifecycle.forwarding.sync

import org.sisioh.dddbase.core.lifecycle.{EntityIOContext, EntitiesChunk}
import org.sisioh.dddbase.core.lifecycle.sync.{SyncEntityReadableAsChunk, SyncEntityReader}
import org.sisioh.dddbase.core.model.{Identifier, Entity}
import scala.util.Try

/**
 * [[org.sisioh.dddbase.core.lifecycle.sync.SyncEntityReadableAsChunk]]のデコレータ。
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait ForwardingSyncEntityReadableAsChunk[ID <: Identifier[_], E <: Entity[ID]]
  extends SyncEntityReadableAsChunk[ID, E] {
  this: SyncEntityReader[ID, E] =>

  type Delegate <: SyncEntityReadableAsChunk[ID, E]

  /**
   * デリゲート。
   */
  protected val delegate: Delegate

  def resolveAsChunk(index: Int, maxEntities: Int)
                  (implicit ctx: Ctx): Try[EntitiesChunk[ID, E]] =
    delegate.resolveAsChunk(index, maxEntities)

}


