package org.sisioh.dddbase.core.lifecycle.forwarding

import org.sisioh.dddbase.core.lifecycle.{EntitiesChunk, SyncEntityReader, SyncEntityReaderByChunk}
import org.sisioh.dddbase.core.model.{Identity, Entity}
import scala.util.Try

trait ForwardingSyncEntityReaderByChunk[ID <: Identity[_], T <: Entity[ID]] extends SyncEntityReaderByChunk[ID, T] {
  this: SyncEntityReader[ID, T] =>

  protected val delegateEntityReaderByChunk: SyncEntityReaderByChunk[ID, T]

  def resolveChunk(index: Int, maxEntities: Int): Try[EntitiesChunk[ID, T]] =
    delegateEntityReaderByChunk.resolveChunk(index, maxEntities)
}


