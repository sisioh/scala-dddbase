package org.sisioh.dddbase.core.lifecycle.async

import org.sisioh.dddbase.core.lifecycle.{EntityIOContext, EntityIO}
import scala.concurrent.{ExecutionContext, Future}

/**
 * [[org.sisioh.dddbase.core.lifecycle.EntityIO]]の非同期版。
 */
trait AsyncEntityIO extends EntityIO {

  /**
   * [[org.sisioh.dddbase.core.lifecycle.EntityIOContext]]から
   * [[scala.concurrent.ExecutionContext]]を取得する。
   *
   * @param ctx [[org.sisioh.dddbase.core.lifecycle.EntityIOContext]]
   * @return [[scala.concurrent.ExecutionContext]]
   */
  protected def getExecutionContext(ctx: EntityIOContext[Future]): ExecutionContext = {
    ctx match {
      case actx: AsyncEntityIOContext => actx.executor
      case _ => throw new IllegalArgumentException(s"$ctx is type miss match. please set to AsyncEntityIOContext.")
    }
  }

}
