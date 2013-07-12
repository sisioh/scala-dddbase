package org.sisioh.dddbase.core.lifecycle.forwarding.sync

import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.util.Try
import org.sisioh.dddbase.core.lifecycle.sync.{SyncEntityReaderByOption, SyncEntityReader}

trait ForwardingSyncEntityReaderByOption[ID <: Identity[_], T <: Entity[ID]]
  extends SyncEntityReaderByOption[ID, T] {
  this: SyncEntityReader[ID, T] =>

  protected val delegateEntityReaderByOption: SyncEntityReaderByOption[ID, T]

  def resolveOption(identity: ID): Try[Option[T]] = delegateEntityReaderByOption.resolveOption(identity)

}
