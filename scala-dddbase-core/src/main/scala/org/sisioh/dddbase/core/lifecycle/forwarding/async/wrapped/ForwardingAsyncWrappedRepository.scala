package org.sisioh.dddbase.core.lifecycle.forwarding.async.wrapped

import org.sisioh.dddbase.core.lifecycle.async.AsyncRepository
import org.sisioh.dddbase.core.lifecycle.sync.SyncRepository
import org.sisioh.dddbase.core.model.{Entity, Identity}

trait ForwardingAsyncWrappedRepository[ID <: Identity[_], E <: Entity[ID]]
  extends ForwardingAsyncWrappedEntityReader[ID, E]
  with ForwardingAsyncWrappedEntityWriter[ID, E]
  with AsyncRepository[ID, E] {

  type Delegate <: SyncRepository[ID, E]

}
