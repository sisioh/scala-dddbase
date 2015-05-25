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
package org.sisioh.dddbase.lifecycle.forwarding.sync.wrapped

import org.sisioh.dddbase.core.lifecycle.async.AsyncEntityWriter
import org.sisioh.dddbase.core.lifecycle.sync.{ SyncEntityWriter, SyncResultWithEntity }
import org.sisioh.dddbase.core.model.{ Entity, Identifier }

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.util.Try

/**
 * `AsyncEntityWriter`を`SyncEntityWriter`としてラップするためのデコレータ。
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait SyncWrappedAsyncEntityWriter[ID <: Identifier[_], E <: Entity[ID]]
    extends SyncEntityWriter[ID, E] with SyncWrappedAsyncEntityIO {

  type Delegate <: AsyncEntityWriter[ID, E]

  /**
   * デリゲート。
   */
  protected val delegate: Delegate

  protected val timeout: Duration

  protected def createInstance(state: (Delegate#This, Option[E])): (This, Option[E])

  def store(entity: E)(implicit ctx: Ctx): Try[Result] = Try {
    implicit val asyncEntityIOContext = getAsyncEntityIOContext(ctx)
    val resultWithEntity = Await.result(delegate.store(entity), timeout)
    val _entity = Some(resultWithEntity.entity.asInstanceOf[E])
    val result = createInstance((resultWithEntity.result.asInstanceOf[Delegate#This], _entity))
    SyncResultWithEntity[This, ID, E](result._1.asInstanceOf[This], result._2.get)
  }

  def deleteBy(identifier: ID)(implicit ctx: Ctx): Try[Result] = Try {
    implicit val asyncEntityIOContext = getAsyncEntityIOContext(ctx)
    val resultWithEntity = Await.result(delegate.deleteBy(identifier), timeout)
    val _entity = Some(resultWithEntity.entity.asInstanceOf[E])
    val result = createInstance((resultWithEntity.result.asInstanceOf[Delegate#This], _entity))
    SyncResultWithEntity[This, ID, E](result._1.asInstanceOf[This], result._2.get)
  }

}
