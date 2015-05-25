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

import org.sisioh.dddbase.core.lifecycle.EntityIO
import scala.concurrent.{ ExecutionContext, Future }

/**
 * `org.sisioh.dddbase.core.lifecycle.EntityIO`の非同期版。
 */
trait AsyncEntityIO extends EntityIO[Future] {

  /**
   * `org.sisioh.dddbase.core.lifecycle.EntityIOContext`から
   * `scala.concurrent.ExecutionContext`を取得する。
   *
   * @param ctx `org.sisioh.dddbase.core.lifecycle.EntityIOContext`
   * @return `scala.concurrent.ExecutionContext`
   */
  protected def getExecutionContext(ctx: Ctx): ExecutionContext = {
    ctx match {
      case actx: AsyncEntityIOContext => actx.executor
      case _                          => throw new IllegalArgumentException(s"$ctx is type miss match. please set to AsyncEntityIOContext.")
    }
  }

}
