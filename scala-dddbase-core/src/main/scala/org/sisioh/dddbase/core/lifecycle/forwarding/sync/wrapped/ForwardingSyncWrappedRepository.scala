package org.sisioh.dddbase.core.lifecycle.forwarding.sync.wrapped

import org.sisioh.dddbase.core.model.{Entity, Identity}
import org.sisioh.dddbase.core.lifecycle.async.AsyncRepository
import org.sisioh.dddbase.core.lifecycle.sync.SyncRepository

trait ForwardingSyncWrappedRepository[ID <: Identity[_], E <: Entity[ID]]
  extends ForwardingSyncWrappedEntityReader[ID, E]
  with ForwardingSyncWrappedEntityWriter[ID, E]
  with SyncRepository[ID, E] {

  type Delegate <: AsyncRepository[ID, E]

}
