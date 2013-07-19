package org.sisioh.dddbase.core.lifecycle.forwarding.sync

import org.sisioh.dddbase.core.lifecycle.EntitiesChunk
import org.sisioh.dddbase.core.model.{Identity, Entity}
import scala.util.Try
import org.sisioh.dddbase.core.lifecycle.sync.{SyncEntityReadableByChunk, SyncEntityReader}

/**
 * [[org.sisioh.dddbase.core.lifecycle.sync.SyncEntityReadableByChunk]]のデコレータ。
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait ForwardingSyncEntityReadableByChunk[ID <: Identity[_], E <: Entity[ID]]
  extends SyncEntityReadableByChunk[ID, E] {
  this: SyncEntityReader[ID, E] =>

  /**
   * デリゲート。
   */
  protected val delegateEntityReaderByChunk: SyncEntityReadableByChunk[ID, E]

  def resolveChunk(index: Int, maxEntities: Int): Try[EntitiesChunk[ID, E]] =
    delegateEntityReaderByChunk.resolveChunk(index, maxEntities)

}


