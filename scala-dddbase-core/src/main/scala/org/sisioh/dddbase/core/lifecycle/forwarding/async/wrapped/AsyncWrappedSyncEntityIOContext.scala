package org.sisioh.dddbase.core.lifecycle.forwarding.async.wrapped

import org.sisioh.dddbase.core.lifecycle.sync.SyncEntityIOContext
import org.sisioh.dddbase.core.lifecycle.async.AsyncEntityIOContext
import scala.concurrent.ExecutionContext

/**
 * [[org.sisioh.dddbase.core.lifecycle.async.AsyncEntityIOContext]]のラッパー版。
 */
trait AsyncWrappedSyncEntityIOContext extends AsyncEntityIOContext {

  /**
   * [[org.sisioh.dddbase.core.lifecycle.sync.SyncEntityIOContext]]
   */
  val syncEntityIOContext: SyncEntityIOContext

}

/**
 * コンパニオンオブジェクト。
 */
object AsyncWrappedSyncEntityIOContext {

  /**
   * ファクトリメソッド。
   *
   * @param syncEntityIOContext [[org.sisioh.dddbase.core.lifecycle.sync.SyncEntityIOContext]]
   * @param executor [[scala.concurrent.ExecutionContext]]
   * @return [[org.sisioh.dddbase.core.lifecycle.forwarding.async.wrapped.AsyncWrappedSyncEntityIOContext]]
   */
  def apply(syncEntityIOContext: SyncEntityIOContext = SyncEntityIOContext)
           (implicit executor: ExecutionContext): AsyncWrappedSyncEntityIOContext =
    new AsyncWrappedSyncEntityIOContextImpl(syncEntityIOContext)

  /**
   * エクストラクタメソッド。
   *
   * @param asyncWrappedEntityIOContext [[org.sisioh.dddbase.core.lifecycle.forwarding.async.wrapped.AsyncWrappedSyncEntityIOContext]]
   * @return 構成要素
   */
  def unapply(asyncWrappedEntityIOContext: AsyncWrappedSyncEntityIOContext): Option[(SyncEntityIOContext)] =
    Some(asyncWrappedEntityIOContext.syncEntityIOContext)

}

private[wrapped]
case class AsyncWrappedSyncEntityIOContextImpl
(syncEntityIOContext: SyncEntityIOContext = SyncEntityIOContext)
(implicit val executor: ExecutionContext)
  extends AsyncWrappedSyncEntityIOContext

