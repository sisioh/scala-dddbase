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
package org.sisioh.dddbase.lifecycle.forwarding.async

import org.sisioh.dddbase.core.lifecycle.async.{ AsyncResultWithEntity, AsyncEntityWriter }
import org.sisioh.dddbase.core.model.{ Entity, Identifier }
import scala.concurrent.Future

/**
 * `AsyncEntityWriter`のデコレータ。
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait ForwardingAsyncEntityWriter[ID <: Identifier[_], E <: Entity[ID]]
    extends AsyncEntityWriter[ID, E] {

  type Delegate <: AsyncEntityWriter[ID, E]

  /**
   * デリゲート。
   */
  protected val delegate: Delegate

  /**
   * 新しいインスタンスを作成する。
   *
   * @param state 新しい状態のデリゲートとエンティティ
   * @return 新しいインスタンスとエンティティ
   */
  protected def createInstance(state: Future[(Delegate#This, Option[E])]): Future[(This, Option[E])]

  def store(entity: E)(implicit ctx: Ctx): Future[Result] = {
    implicit val executor = getExecutionContext(ctx)
    val state = delegate.store(entity).map {
      result =>
        (result.result.asInstanceOf[Delegate#This], Some(result.entity))
    }
    val instance = createInstance(state)
    instance.map {
      e =>
        AsyncResultWithEntity(e._1, e._2.get)
    }
  }

  def deleteBy(identifier: ID)(implicit ctx: Ctx): Future[Result] = {
    implicit val executor = getExecutionContext(ctx)
    val state = delegate.deleteBy(identifier).map {
      result =>
        (result.result.asInstanceOf[Delegate#This], Some(result.entity))
    }
    val instance = createInstance(state)
    instance.map {
      e =>
        AsyncResultWithEntity(e._1, e._2.get)
    }
  }

}
