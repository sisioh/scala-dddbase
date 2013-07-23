package org.sisioh.dddbase.core.lifecycle.forwarding.async.wrapped

import org.sisioh.dddbase.core.lifecycle.async.AsyncEntityReader
import org.sisioh.dddbase.core.lifecycle.sync.SyncEntityReader
import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.concurrent._

/**
 * [[org.sisioh.dddbase.core.lifecycle.sync.SyncEntityReader]]をラップし
 * [[org.sisioh.dddbase.core.lifecycle.async.AsyncEntityReader]]にするための
 * デコレータ。
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait ForwardingAsyncWrappedEntityReader[ID <: Identity[_], E <: Entity[ID]]
  extends AsyncEntityReader[ID, E] {

  type Delegate <: SyncEntityReader[ID, E]

  protected val delegate: Delegate

  def resolve(identity: ID) = future {
    delegate.resolve(identity).get
  }

  def contains(identity: ID): Future[Boolean] = future {
    delegate.contains(identity).get
  }

}
