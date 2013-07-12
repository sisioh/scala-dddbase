package org.sisioh.dddbase.core.lifecycle.forwarding.sync

import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.util.Try
import org.sisioh.dddbase.core.lifecycle.sync.SyncEntityReader

trait ForwardingSyncEntityReader[ID <: Identity[_], T <: Entity[ID]]
  extends SyncEntityReader[ID, T] {

  protected val delegateEntityReader: SyncEntityReader[ID, T]

  def resolve(identity: ID): Try[T] = delegateEntityReader.resolve(identity)

  def contains(identity: ID): Try[Boolean] = delegateEntityReader.contains(identity)

}
