package org.sisioh.dddbase.core.lifecycle.forwarding.sync.wrapped

import org.sisioh.dddbase.core.model.{Entity, Identity}
import org.sisioh.dddbase.core.lifecycle.sync.SyncEntityReader
import org.sisioh.dddbase.core.lifecycle.async.AsyncEntityReader
import scala.util.Try
import scala.concurrent.duration.Duration
import scala.concurrent.{Future, Await}
import org.sisioh.dddbase.core.lifecycle.EntityIOContext

/**
 * [[org.sisioh.dddbase.core.lifecycle.async.AsyncEntityReader]]を
 * [[org.sisioh.dddbase.core.lifecycle.sync.SyncEntityReader]]として
 * ラップするためのデコレータ。
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait SyncWrappedAsyncEntityReader[CTX <: EntityIOContext[Try], ID <: Identity[_], E <: Entity[ID]]
  extends SyncEntityReader[CTX, ID, E] with SyncWrappedAsyncEntityIO {

  type Delegate <: AsyncEntityReader[EntityIOContext[Future], ID, E]

  protected val delegate: Delegate

  protected val timeOut: Duration

  def resolve(identity: ID)(implicit ctx: CTX): Try[E] = Try {
    implicit val asyncEntityIOContext = getAsyncEntityIOContext(ctx)
    Await.result(delegate.resolve(identity), timeOut)
  }

  def containsByIdentity(identity: ID)(implicit ctx: CTX): Try[Boolean] = Try {
    implicit val asyncEntityIOContext = getAsyncEntityIOContext(ctx)
    Await.result(delegate.containsByIdentity(identity), timeOut)
  }

}
