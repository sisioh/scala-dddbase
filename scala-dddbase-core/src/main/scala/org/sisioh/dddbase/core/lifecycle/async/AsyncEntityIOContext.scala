package org.sisioh.dddbase.core.lifecycle.async

import org.sisioh.dddbase.core.lifecycle.EntityIOContext
import scala.concurrent.{Future, ExecutionContext}

/**
 * [[org.sisioh.dddbase.core.lifecycle.EntityIOContext]]の非同期版。
 */
trait AsyncEntityIOContext extends EntityIOContext[Future] {

  /**
   * [[scala.concurrent.ExecutionContext]]
   */
  val executor: ExecutionContext

}

/**
 * コンパニオンオブジェクト。
 */
object AsyncEntityIOContext {

  /**
   * ファクトリメソッド。
   *
   * @param executor [[scala.concurrent.ExecutionContext]]
   * @return [[org.sisioh.dddbase.core.lifecycle.async.AsyncEntityIOContext]]
   */
  def apply()(implicit executor: ExecutionContext): AsyncEntityIOContext =
    new AsyncEntityIOContextImpl()

  /**
   * エクストラクタメソッド。
   *
   * @param asyncEntityIOContext [[org.sisioh.dddbase.core.lifecycle.async.AsyncEntityIOContext]]
   * @return 構成要素
   */
  def unapply(asyncEntityIOContext: AsyncEntityIOContext): Option[(ExecutionContext)] =
    Some(asyncEntityIOContext.executor)

}

private[async]
case class AsyncEntityIOContextImpl(implicit val executor: ExecutionContext)
  extends AsyncEntityIOContext
