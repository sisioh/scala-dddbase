package org.sisioh.dddbase.core.lifecycle.forwarding.sync.wrapped

import org.sisioh.dddbase.core.model.{Entity, Identity}
import org.sisioh.dddbase.core.lifecycle.sync.SyncEntityReader
import org.sisioh.dddbase.core.lifecycle.async.AsyncEntityReader
import scala.util.Try
import scala.concurrent.duration.Duration
import scala.concurrent.Await

trait ForwardingSyncWrappedEntityReader[ID <: Identity[_], E <: Entity[ID]]
  extends SyncEntityReader[ID, E] {

  type Delegate <: AsyncEntityReader[ID, E]

  protected val delegate: Delegate

  protected val timeOut: Duration

  def resolve(identity: ID): Try[E] = Try {
    Await.result(delegate.resolve(identity), timeOut)
  }

  def contains(identity: ID): Try[Boolean] = Try{
    Await.result(delegate.contains(identity), timeOut)
  }

}
