package org.sisioh.dddbase.core.lifecycle.forwarding.async

import org.sisioh.dddbase.core.lifecycle.EntitiesChunk
import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.concurrent.{Future, ExecutionContext}
import org.sisioh.dddbase.core.lifecycle.async.{AsyncEntityReaderByChunk, AsyncEntityReader}

trait ForwardingAsyncEntityReaderByChunk[ID <: Identity[_], E <: Entity[ID]] extends AsyncEntityReaderByChunk[ID, E] {
  this: AsyncEntityReader[ID, E] =>

  protected val delegateAsyncEntityReaderByChunk: AsyncEntityReaderByChunk[ID, E]

  def resolveChunk(index: Int, maxEntities: Int): Future[EntitiesChunk[ID, E]] =
    delegateAsyncEntityReaderByChunk.resolveChunk(index, maxEntities)

}
