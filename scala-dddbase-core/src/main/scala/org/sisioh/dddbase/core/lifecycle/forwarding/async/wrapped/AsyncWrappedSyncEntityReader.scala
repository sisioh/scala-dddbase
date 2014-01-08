package org.sisioh.dddbase.core.lifecycle.forwarding.async.wrapped

import org.sisioh.dddbase.core.lifecycle.async.AsyncEntityReader
import org.sisioh.dddbase.core.lifecycle.sync.SyncEntityReader
import org.sisioh.dddbase.core.model.{Entity, Identifier}
import scala.concurrent._
import org.sisioh.dddbase.core.lifecycle.EntityIOContext

/**
 * [[org.sisioh.dddbase.core.lifecycle.sync.SyncEntityReader]]を
 * [[org.sisioh.dddbase.core.lifecycle.async.AsyncEntityReader]]として
 * ラップするためのデコレータ。
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait AsyncWrappedSyncEntityReader[ID <: Identifier[_], E <: Entity[ID]]
  extends AsyncEntityReader[ID, E] with AsyncWrappedSyncEntityIO {

  type Delegate <: SyncEntityReader[ID, E]

  protected val delegate: Delegate

  def resolveBy(identity: ID)(implicit ctx: Ctx) = {
    val asyncCtx = getAsyncWrappedEntityIOContext(ctx)
    implicit val executor = asyncCtx.executor
    future {
      implicit val syncCtx = asyncCtx.syncEntityIOContext
      delegate.resolveBy(identity).get
    }
  }

  def existBy(identity: ID)(implicit ctx: Ctx): Future[Boolean] = {
    val asyncCtx = getAsyncWrappedEntityIOContext(ctx)
    implicit val executor = asyncCtx.executor
    future {
      implicit val syncCtx = asyncCtx.syncEntityIOContext
       delegate.existBy(identity).get
    }
  }

}
