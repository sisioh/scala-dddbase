package org.sisioh.dddbase.core.lifecycle.forwarding.sync

import org.sisioh.dddbase.core.lifecycle.EntitiesChunk
import org.sisioh.dddbase.core.model.{Identity, Entity}
import scala.util.Try
import org.sisioh.dddbase.core.lifecycle.sync.{SyncEntityReaderByChunk, SyncEntityReader}

/**
 * [[org.sisioh.dddbase.core.lifecycle.sync.SyncEntityReaderByChunk]]のデコレータ。
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait ForwardingSyncEntityReaderByChunk[ID <: Identity[_], E <: Entity[ID]]
  extends SyncEntityReaderByChunk[ID, E] {
  this: SyncEntityReader[ID, E] =>

  protected val delegateEntityReaderByChunk: SyncEntityReaderByChunk[ID, E]

  def resolveChunk(index: Int, maxEntities: Int): Try[EntitiesChunk[ID, E]] =
    delegateEntityReaderByChunk.resolveChunk(index, maxEntities)

}


