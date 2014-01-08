package org.sisioh.dddbase.core.lifecycle.forwarding.async.wrapped

import org.sisioh.dddbase.core.lifecycle.async.AsyncEntityIO

/**
 * ラッパー用の[[org.sisioh.dddbase.core.lifecycle.async.AsyncEntityIO]]の実装。
 */
trait AsyncWrappedSyncEntityIO extends AsyncEntityIO {

  /**
   * [[org.sisioh.dddbase.core.lifecycle.EntityIOContext]]を
   * [[org.sisioh.dddbase.core.lifecycle.forwarding.async.wrapped.AsyncWrappedSyncEntityIOContext]]に変換する。
   *
   * @param ctx [[org.sisioh.dddbase.core.lifecycle.EntityIOContext]]
   * @return [[org.sisioh.dddbase.core.lifecycle.forwarding.async.wrapped.AsyncWrappedSyncEntityIOContext]]
   */
  protected def getAsyncWrappedEntityIOContext(ctx: Ctx): AsyncWrappedSyncEntityIOContext =
    ctx match {
      case result: AsyncWrappedSyncEntityIOContext => result
      case _ => throw new IllegalArgumentException(s"$ctx is type miss match. please set to AsyncWrappedSyncEntityIOContext.")
    }

}
