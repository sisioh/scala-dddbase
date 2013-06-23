package org.sisioh.dddbase.core.lifecycle.forwarding

import org.sisioh.dddbase.core.lifecycle.{EntitiesChunk, EntityReader, EntityReaderByChunk}
import org.sisioh.dddbase.core.model.{Identity, Entity}
import scala.util.Try

trait ForwardingEntityReaderByChunk[ID <: Identity[_], T <: Entity[ID]] extends EntityReaderByChunk[ID, T] {
  this: EntityReader[ID, T] =>

  protected val delegateEntityReaderByChunk: EntityReaderByChunk[ID, T]

  def resolveChunk(index: Int, maxEntities: Int): Try[EntitiesChunk[ID, T]] =
    delegateEntityReaderByChunk.resolveChunk(index, maxEntities)
}


