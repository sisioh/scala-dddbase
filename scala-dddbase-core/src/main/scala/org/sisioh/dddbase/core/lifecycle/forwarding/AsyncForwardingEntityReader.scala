package org.sisioh.dddbase.core.lifecycle.forwarding

import org.sisioh.dddbase.core.lifecycle.AsyncEntityReader
import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.concurrent.{Future, ExecutionContext}

trait AsyncForwardingEntityReader[ID <: Identity[_], T <: Entity[ID]] extends AsyncEntityReader[ID, T] {

  protected val delegateAsyncEntityReader: AsyncEntityReader[ID, T]

  def resolve(identity: ID)(implicit executor: ExecutionContext) =
    delegateAsyncEntityReader.resolve(identity)

  def contains(identity: ID)(implicit executor: ExecutionContext): Future[Boolean] =
    delegateAsyncEntityReader.contains(identity)

}
