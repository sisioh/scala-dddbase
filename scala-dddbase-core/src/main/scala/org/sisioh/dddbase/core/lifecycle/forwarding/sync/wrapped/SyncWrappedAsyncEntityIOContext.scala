package org.sisioh.dddbase.core.lifecycle.forwarding.sync.wrapped

import org.sisioh.dddbase.core.lifecycle.async.AsyncEntityIOContext
import org.sisioh.dddbase.core.lifecycle.sync.SyncEntityIOContext

/**
 * [[org.sisioh.dddbase.core.lifecycle.async.AsyncEntityIOContext]]を
 * [[org.sisioh.dddbase.core.lifecycle.sync.SyncEntityIOContext]]として
 * ラップするトレイト。
 */
trait SyncWrappedAsyncEntityIOContext extends SyncEntityIOContext {

  /**
   * [[org.sisioh.dddbase.core.lifecycle.async.AsyncEntityIOContext]]
   */
  val asyncEntityIOContext: AsyncEntityIOContext

}

/**
 * コンパニオンオブジェクト。
 */
object SyncWrappedAsyncEntityIOContext {

  /**
   * ファクトリメソッド。
   *
   * @param asyncEntityIOContext [[org.sisioh.dddbase.core.lifecycle.async.AsyncEntityIOContext]]
   * @return [[org.sisioh.dddbase.core.lifecycle.forwarding.sync.wrapped.SyncWrappedAsyncEntityIOContext]]
   */
  def apply(asyncEntityIOContext: AsyncEntityIOContext): SyncWrappedAsyncEntityIOContext =
    new SyncWrappedEntityIOContextImpl(asyncEntityIOContext)

  /**
   * エクストラクタメソッド。
   *
   * @param syncWrappedEntityIOContext [[org.sisioh.dddbase.core.lifecycle.forwarding.sync.wrapped.SyncWrappedAsyncEntityIOContext]]
   * @return 構成要素
   */
  def unapply(syncWrappedEntityIOContext: SyncWrappedAsyncEntityIOContext): Option[(AsyncEntityIOContext)] =
    Some(syncWrappedEntityIOContext.asyncEntityIOContext)

}

private[wrapped]
case class SyncWrappedEntityIOContextImpl
(asyncEntityIOContext: AsyncEntityIOContext)
  extends SyncWrappedAsyncEntityIOContext

