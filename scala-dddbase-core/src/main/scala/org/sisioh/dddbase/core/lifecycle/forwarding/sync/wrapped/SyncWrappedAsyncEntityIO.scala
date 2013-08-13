package org.sisioh.dddbase.core.lifecycle.forwarding.sync.wrapped

import org.sisioh.dddbase.core.lifecycle.EntityIOContext
import scala.util.Try
import org.sisioh.dddbase.core.lifecycle.sync.SyncEntityIO
import org.sisioh.dddbase.core.lifecycle.async.AsyncEntityIOContext

/**
 *
 */
trait SyncWrappedAsyncEntityIO extends SyncEntityIO {

  /**
   * [[org.sisioh.dddbase.core.lifecycle.EntityIOContext]]から
   * [[org.sisioh.dddbase.core.lifecycle.async.AsyncEntityIOContext]]を取得する。
   *
   * @param ctx [[org.sisioh.dddbase.core.lifecycle.EntityIOContext]]
   * @return [[org.sisioh.dddbase.core.lifecycle.async.AsyncEntityIOContext]]
   */
  protected def getAsyncEntityIOContext(ctx: EntityIOContext[Try]): AsyncEntityIOContext =
    ctx match {
      case SyncWrappedAsyncEntityIOContext(async) => async
      case _ => throw new IllegalArgumentException(s"$ctx is type miss match. please set to SyncWrappedAsyncEntityIOContext.")
    }

}
