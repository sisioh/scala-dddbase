package org.sisioh.dddbase.core.lifecycle.forwarding.async

import org.sisioh.dddbase.core.lifecycle.EntitiesChunk
import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.concurrent.{Future, ExecutionContext}
import org.sisioh.dddbase.core.lifecycle.async.{AsyncEntityReaderByChunk, AsyncEntityReader}

trait ForwardingAsyncEntityReaderByChunk[ID <: Identity[_], T <: Entity[ID]] extends AsyncEntityReaderByChunk[ID, T] {
  this: AsyncEntityReader[ID, T] =>

  protected val delegateAsyncEntityReaderByChunk: AsyncEntityReaderByChunk[ID, T]

  def resolveChunk(index: Int, maxEntities: Int): Future[EntitiesChunk[ID, T]] =
    delegateAsyncEntityReaderByChunk.resolveChunk(index, maxEntities)

}
