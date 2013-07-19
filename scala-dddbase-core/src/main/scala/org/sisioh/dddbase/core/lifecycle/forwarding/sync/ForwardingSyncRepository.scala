package org.sisioh.dddbase.core.lifecycle.forwarding.sync

import org.sisioh.dddbase.core.lifecycle.sync.{SyncEntityWriter, SyncEntityReader, SyncRepository}
import org.sisioh.dddbase.core.model.{Entity, Identity}

trait ForwardingSyncRepository[ID <: Identity[_], E <: Entity[ID]]
  extends ForwardingSyncEntityReader[ID, E]
  with ForwardingSyncEntityWriter[ID, E]
  with SyncRepository[ID, E] {

  type This <: ForwardingSyncRepository[ID, E]

  protected val delegateRepository: SyncRepository[ID, E]
  protected val delegateEntityReader: SyncEntityReader[ID, E] = delegateRepository
  protected val delegateEntityWriter: SyncEntityWriter[ID, E] = delegateRepository

}
