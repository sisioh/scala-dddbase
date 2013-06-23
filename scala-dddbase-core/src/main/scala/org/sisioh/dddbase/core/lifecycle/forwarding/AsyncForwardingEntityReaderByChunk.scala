package org.sisioh.dddbase.core.lifecycle.forwarding

import org.sisioh.dddbase.core.lifecycle.{EntitiesChunk, AsyncEntityReaderByChunk, AsyncEntityReader}
import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.concurrent.{Future, ExecutionContext}

trait AsyncForwardingEntityReaderByChunk[ID <: Identity[_], T <: Entity[ID]] extends AsyncEntityReaderByChunk[ID, T] {
  this: AsyncEntityReader[ID, T] =>

  protected val delegateAsyncEntityReaderByChunk: AsyncEntityReaderByChunk[ID, T]

  def resolveChunk(index: Int, maxEntities: Int)(implicit executor: ExecutionContext): Future[EntitiesChunk[ID, T]] =
    delegateAsyncEntityReaderByChunk.resolveChunk(index, maxEntities)

}
