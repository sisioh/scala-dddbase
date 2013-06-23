package org.sisioh.dddbase.core.lifecycle.forwarding

import org.sisioh.dddbase.core.lifecycle.{AsyncEntityReader, AsyncEntityReaderByOption}
import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.concurrent.{ExecutionContext, Future}

trait AsyncForwardingEntityReaderByOption[ID <: Identity[_], T <: Entity[ID]]
  extends AsyncEntityReaderByOption[ID, T] {
  this: AsyncEntityReader[ID, T] =>

  protected val delegateAsyncEntityReaderByOption: AsyncEntityReaderByOption[ID, T]

  def resolveOption(identity: ID)(implicit executor: ExecutionContext): Future[Option[T]] =
    delegateAsyncEntityReaderByOption.resolveOption(identity)

}
