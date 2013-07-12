package org.sisioh.dddbase.core.lifecycle.forwarding

import org.sisioh.dddbase.core.lifecycle.SyncEntityReader
import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.util.Try

trait ForwardingSyncEntityReader[ID <: Identity[_], T <: Entity[ID]] extends SyncEntityReader[ID, T] {

  protected val delegateEntityReader: SyncEntityReader[ID, T]

  def resolve(identity: ID): Try[T] = delegateEntityReader.resolve(identity)

  def contains(identity: ID): Try[Boolean] = delegateEntityReader.contains(identity)

}
