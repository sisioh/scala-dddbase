package org.sisioh.dddbase.core.lifecycle.forwarding.async

import org.sisioh.dddbase.core.model.{Entity, Identity}
import org.sisioh.dddbase.core.lifecycle.async.{AsyncEntityWriter, AsyncEntityReader, AsyncRepository}

trait ForwardingAsyncRepository[R <: AsyncRepository[_, ID, T], ID <: Identity[_], T <: Entity[ID]]
  extends ForwardingAsyncEntityReader[ID, T]
  with ForwardingAsyncEntityWriter[R, ID, T]
  with AsyncRepository[R, ID, T]{

  protected val delegateAsyncRepository: AsyncRepository[_, ID, T]
  protected val delegateAsyncEntityReader: AsyncEntityReader[ID, T] = delegateAsyncRepository
  protected val delegateAsyncEntityWriter: AsyncEntityWriter[_, ID, T] = delegateAsyncRepository

}


