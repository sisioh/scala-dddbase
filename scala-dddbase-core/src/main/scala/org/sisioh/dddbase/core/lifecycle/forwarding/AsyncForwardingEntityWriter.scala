package org.sisioh.dddbase.core.lifecycle.forwarding

import org.sisioh.dddbase.core.lifecycle.AsyncEntityWriter
import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.concurrent.{Future, ExecutionContext}

trait AsyncForwardingEntityWriter[R <: AsyncEntityWriter[_, ID, T], ID <: Identity[_], T <: Entity[ID]]
  extends AsyncEntityWriter[R, ID, T] {

  protected val delegateAsyncEntityWriter: AsyncEntityWriter[_, ID, T]

  protected def createInstance(state: Future[AsyncEntityWriter[_, ID, T]]): Future[R]

  def store(entity: T)(implicit executor: ExecutionContext): Future[R] =
    createInstance(delegateAsyncEntityWriter.store(entity).map(_.asInstanceOf[AsyncEntityWriter[R, ID, T]]))

  def delete(identity: ID)(implicit executor: ExecutionContext): Future[R] =
    createInstance(delegateAsyncEntityWriter.delete(identity).map(_.asInstanceOf[AsyncEntityWriter[R, ID, T]]))

}
