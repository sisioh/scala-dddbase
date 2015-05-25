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
package org.sisioh.dddbase.lifecycle.forwarding.sync

import org.sisioh.dddbase.core.lifecycle.sync.{ SyncEntityWriter, SyncResultWithEntity }
import org.sisioh.dddbase.core.model.{ Entity, Identifier }
import scala.util.Try

/**
 * `SyncEntityWriter`のデリゲート。
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait ForwardingSyncEntityWriter[ID <: Identifier[_], E <: Entity[ID]]
    extends SyncEntityWriter[ID, E] {

  type This <: ForwardingSyncEntityWriter[ID, E]

  type Delegate <: SyncEntityWriter[ID, E]

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
  protected def createInstance(state: Try[(Delegate#This, Option[E])]): Try[(This, Option[E])]

  def store(entity: E)(implicit ctx: Ctx): Try[Result] = {
    createInstance(
      delegate.store(entity).map {
        e =>
          (e.result.asInstanceOf[Delegate#This], Some(e.entity))
      }
    ).map(e => SyncResultWithEntity(e._1, e._2.get))
  }

  def deleteBy(identifier: ID)(implicit ctx: Ctx): Try[Result] = {
    createInstance(
      delegate.deleteBy(identifier).map {
        e =>
          (e.result.asInstanceOf[Delegate#This], Some(e.entity))
      }
    ).map(e => SyncResultWithEntity(e._1, e._2.get))
  }

}
