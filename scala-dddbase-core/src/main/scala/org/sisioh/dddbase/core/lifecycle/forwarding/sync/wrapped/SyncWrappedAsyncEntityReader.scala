package org.sisioh.dddbase.core.lifecycle.forwarding.sync.wrapped

import org.sisioh.dddbase.core.model.{Entity, Identity}
import org.sisioh.dddbase.core.lifecycle.sync.SyncEntityReader
import org.sisioh.dddbase.core.lifecycle.async.AsyncEntityReader
import scala.util.Try
import scala.concurrent.duration.Duration
import scala.concurrent.Await
import org.sisioh.dddbase.core.lifecycle.EntityIOContext

/**
 * [[org.sisioh.dddbase.core.lifecycle.async.AsyncEntityReader]]を
 * [[org.sisioh.dddbase.core.lifecycle.sync.SyncEntityReader]]として
 * ラップするためのデコレータ。
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait SyncWrappedAsyncEntityReader[ID <: Identity[_], E <: Entity[ID]]
  extends SyncEntityReader[ID, E] with SyncWrappedAsyncEntityIO {

  type Delegate <: AsyncEntityReader[ID, E]

  protected val delegate: Delegate

  protected val timeOut: Duration

  def resolve(identity: ID)(implicit ctx: Ctx): Try[E] = Try {
    implicit val asyncEntityIOContext = getAsyncEntityIOContext(ctx)
    Await.result(delegate.resolve(identity), timeOut)
  }

  def existByIdentifier(identity: ID)(implicit ctx: Ctx): Try[Boolean] = Try {
    implicit val asyncEntityIOContext = getAsyncEntityIOContext(ctx)
    Await.result(delegate.existByIdentifier(identity), timeOut)
  }

}
