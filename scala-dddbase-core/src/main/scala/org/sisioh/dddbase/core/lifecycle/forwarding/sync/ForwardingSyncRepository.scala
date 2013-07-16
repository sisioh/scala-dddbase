package org.sisioh.dddbase.core.lifecycle.forwarding.sync

import org.sisioh.dddbase.core.model.{Entity, Identity}
import org.sisioh.dddbase.core.lifecycle.sync.{SyncEntityWriter, SyncEntityReader, SyncRepository}

trait ForwardingSyncRepository[+R <: SyncRepository[_, ID, T], ID <: Identity[_], T <: Entity[ID]]
  extends ForwardingSyncEntityReader[ID, T]
  with ForwardingSyncEntityWriter[R, ID, T]
  with SyncRepository[R, ID, T] {

  protected val delegateRepository: SyncRepository[_, ID, T]
  protected val delegateEntityReader: SyncEntityReader[ID, T] = delegateRepository
  protected val delegateEntityWriter: SyncEntityWriter[_, ID, T] = delegateRepository

}
