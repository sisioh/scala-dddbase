package org.sisioh.dddbase.core.lifecycle.forwarding.async

import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.concurrent.{ExecutionContext, Future}
import org.sisioh.dddbase.core.lifecycle.async.{AsyncEntityReaderByOption, AsyncEntityReader}

trait ForwardingAsyncEntityReaderByOption[ID <: Identity[_], E <: Entity[ID]]
  extends AsyncEntityReaderByOption[ID, E] {
  this: AsyncEntityReader[ID, E] =>

  protected val delegateAsyncEntityReaderByOption: AsyncEntityReaderByOption[ID, E]

  def resolveOption(identity: ID): Future[Option[E]] =
    delegateAsyncEntityReaderByOption.resolveOption(identity)

}
