package org.sisioh.dddbase.core.lifecycle.forwarding.async

import org.sisioh.dddbase.core.model.{Entity, Identity}
import org.sisioh.dddbase.core.lifecycle.async.{AsyncEntityWriter, AsyncEntityReader, AsyncRepository}

trait ForwardingAsyncRepository[ID <: Identity[_], T <: Entity[ID]]
  extends ForwardingAsyncEntityReader[ID, T]
  with ForwardingAsyncEntityWriter[ID, T]
  with AsyncRepository[ID, T]{

  protected val delegateAsyncRepository: AsyncRepository[ID, T]
  protected val delegateAsyncEntityReader: AsyncEntityReader[ID, T] = delegateAsyncRepository
  protected val delegateAsyncEntityWriter: AsyncEntityWriter[ID, T] = delegateAsyncRepository

}


