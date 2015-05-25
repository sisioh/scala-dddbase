/*
 * Copyright 2011-2013 Sisioh Project and others. (http://www.sisioh.org/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.sisioh.dddbase.core.lifecycle.async

import org.sisioh.dddbase.core.lifecycle.EntityIOContext
import scala.concurrent.{ Future, ExecutionContext }

/**
 * `org.sisioh.dddbase.core.lifecycle.EntityIOContext`の非同期版。
 */
trait AsyncEntityIOContext extends EntityIOContext[Future] {

  /**
   * `scala.concurrent.ExecutionContext`
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
   * @param executor `scala.concurrent.ExecutionContext`
   * @return `org.sisioh.dddbase.core.lifecycle.async.AsyncEntityIOContext`
   */
  def apply()(implicit executor: ExecutionContext): AsyncEntityIOContext =
    new AsyncEntityIOContextImpl()

  /**
   * エクストラクタメソッド。
   *
   * @param asyncEntityIOContext `org.sisioh.dddbase.core.lifecycle.async.AsyncEntityIOContext`
   * @return 構成要素
   */
  def unapply(asyncEntityIOContext: AsyncEntityIOContext): Option[(ExecutionContext)] =
    Some(asyncEntityIOContext.executor)

}

private[async] case class AsyncEntityIOContextImpl(implicit val executor: ExecutionContext)
  extends AsyncEntityIOContext
