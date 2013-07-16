package org.sisioh.dddbase.core.lifecycle.forwarding.async

import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.concurrent.{ExecutionContext, Future}
import org.sisioh.dddbase.core.lifecycle.async.{AsyncEntityReaderByOption, AsyncEntityReader}

trait ForwardingAsyncEntityReaderByOption[ID <: Identity[_], T <: Entity[ID]]
  extends AsyncEntityReaderByOption[ID, T] {
  this: AsyncEntityReader[ID, T] =>

  protected val delegateAsyncEntityReaderByOption: AsyncEntityReaderByOption[ID, T]

  def resolveOption(identity: ID): Future[Option[T]] =
    delegateAsyncEntityReaderByOption.resolveOption(identity)

}
