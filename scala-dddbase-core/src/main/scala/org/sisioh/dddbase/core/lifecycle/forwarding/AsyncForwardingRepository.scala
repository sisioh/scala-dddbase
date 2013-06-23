package org.sisioh.dddbase.core.lifecycle.forwarding

import org.sisioh.dddbase.core.lifecycle._
import org.sisioh.dddbase.core.model.{Entity, Identity}

trait AsyncForwardingRepository[R <: AsyncRepository[_, ID, T], ID <: Identity[_], T <: Entity[ID]]
  extends AsyncForwardingEntityReader[ID, T]
  with AsyncForwardingEntityWriter[R, ID, T]
  with AsyncRepository[R, ID, T]{

  protected val delegateAsyncRepository: AsyncRepository[_, ID, T]
  protected val delegateAsyncEntityReader: AsyncEntityReader[ID, T] = delegateAsyncRepository
  protected val delegateAsyncEntityWriter: AsyncEntityWriter[_, ID, T] = delegateAsyncRepository

}


