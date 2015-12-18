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
package org.sisioh.dddbase.lifecycle.forwarding.async.wrapped

import org.sisioh.dddbase.core.lifecycle.async.AsyncEntityReader
import org.sisioh.dddbase.core.lifecycle.sync.SyncEntityReader
import org.sisioh.dddbase.core.model.{ Entity, Identifier }
import scala.concurrent._
import scala.util.Try

/**
 * `SyncEntityReader`を`AsyncEntityReader`としてラップするためのデコレータ。
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait AsyncWrappedSyncEntityReader[ID <: Identifier[_], E <: Entity[ID]]
    extends AsyncEntityReader[ID, E] with AsyncWrappedSyncEntityIO {

  type Delegate <: SyncEntityReader[ID, E]

  protected val delegate: Delegate

  def resolveBy(identifier: ID)(implicit ctx: Ctx): Future[E] = {
    val asyncCtx = getAsyncWrappedEntityIOContext(ctx)
    implicit val executor = asyncCtx.executor
    Future {
      implicit val syncCtx = asyncCtx.syncEntityIOContext
      delegate.resolveBy(identifier).get
    }
  }

  def existBy(identifier: ID)(implicit ctx: Ctx): Future[Boolean] = {
    val asyncCtx = getAsyncWrappedEntityIOContext(ctx)
    implicit val executor = asyncCtx.executor
    Future {
      implicit val syncCtx = asyncCtx.syncEntityIOContext
      delegate.existBy(identifier).get
    }
  }

}
