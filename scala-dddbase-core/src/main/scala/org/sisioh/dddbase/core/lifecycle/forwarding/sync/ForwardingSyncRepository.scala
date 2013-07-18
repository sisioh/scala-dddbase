package org.sisioh.dddbase.core.lifecycle.forwarding.sync

import org.sisioh.dddbase.core.lifecycle.sync.{SyncEntityWriter, SyncEntityReader, SyncRepository}
import org.sisioh.dddbase.core.model.{Entity, Identity}

trait ForwardingSyncRepository[ID <: Identity[_], T <: Entity[ID]]
  extends ForwardingSyncEntityReader[ID, T]
  with ForwardingSyncEntityWriter[ID, T]
  with SyncRepository[ID, T] {

  type This <: ForwardingSyncRepository[ID, T]

  protected val delegateRepository: SyncRepository[ID, T]
  protected val delegateEntityReader: SyncEntityReader[ID, T] = delegateRepository
  protected val delegateEntityWriter: SyncEntityWriter[ID, T] = delegateRepository

}
