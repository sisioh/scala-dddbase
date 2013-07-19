package org.sisioh.dddbase.core.lifecycle.forwarding.sync

import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.util.Try
import org.sisioh.dddbase.core.lifecycle.sync.SyncEntityReader

trait ForwardingSyncEntityReader[ID <: Identity[_], E <: Entity[ID]]
  extends SyncEntityReader[ID, E] {

  protected val delegateEntityReader: SyncEntityReader[ID, E]

  def resolve(identity: ID): Try[E] = delegateEntityReader.resolve(identity)

  def contains(identity: ID): Try[Boolean] = delegateEntityReader.contains(identity)

}
