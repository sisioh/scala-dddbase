package org.sisioh.dddbase.core.lifecycle.forwarding.async

import org.sisioh.dddbase.core.model.{Entity, Identity}
import org.sisioh.dddbase.core.lifecycle.async.{AsyncEntityWriter, AsyncEntityReader, AsyncRepository}

trait ForwardingAsyncRepository[ID <: Identity[_], E <: Entity[ID]]
  extends ForwardingAsyncEntityReader[ID, E]
  with ForwardingAsyncEntityWriter[ID, E]
  with AsyncRepository[ID, E]{

  protected val delegateAsyncRepository: AsyncRepository[ID, E]
  protected val delegateAsyncEntityReader: AsyncEntityReader[ID, E] = delegateAsyncRepository
  protected val delegateAsyncEntityWriter: AsyncEntityWriter[ID, E] = delegateAsyncRepository

}


