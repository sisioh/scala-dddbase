/*
 * Copyright 2010 TRICREO, Inc. (http://tricreo.jp/)
 * Copyright 2011 Sisioh Project and others. (http://www.sisioh.org/)
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
package org.sisioh.dddbase.lifecycle.memory.async

import org.sisioh.dddbase.core.model.{ Identifier, EntityCloneable, Entity }
import org.sisioh.dddbase.lifecycle.forwarding.async.wrapped.AsyncWrappedSyncEntityIOContext
import scala.concurrent.ExecutionContext
import scala.scalajs.js.annotation.JSExport

/**
 * 汎用的な非同期型オンメモリ不変リポジトリ。
 *
 * @param entities マップ
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
@JSExport
class GenericAsyncRepositoryOnMemory[ID <: Identifier[_], E <: Entity[ID] with EntityCloneable[ID, E] with Ordered[E]](entities: Map[ID, E] = Map.empty[ID, E])
    extends AbstractAsyncRepositoryOnMemory[ID, E](entities) {

  type This = GenericAsyncRepositoryOnMemory[ID, E]

  override protected def createInstance(entities: Map[ID, E]): This =
    new GenericAsyncRepositoryOnMemory(entities)

}

/**
 * コンパニオンオブジェクト。
 */
@JSExport
object GenericAsyncRepositoryOnMemory {

  object Implicits {

    import scala.concurrent.ExecutionContext.Implicits.global

    implicit val defaultEntityIOContext = createEntityIOContext

  }

  /**
   * `org.sisioh.dddbase.core.lifecycle.EntityIOContext`を生成する。
   *
   * @param executor `ExecutionContext`
   * @return `org.sisioh.dddbase.lifecycle.forwarding.async.wrapped.AsyncWrappedSyncEntityIOContext`
   */
  def createEntityIOContext(implicit executor: ExecutionContext) = AsyncWrappedSyncEntityIOContext()

  /**
   * ファクトリメソッド。
   *
   * @param entities マップ
   * @tparam ID 識別子の型
   * @tparam E エンティティの型
   * @return `org.sisioh.dddbase.lifecycle.memory.async.GenericAsyncRepositoryOnMemory`
   */
  def apply[ID <: Identifier[_], E <: Entity[ID] with EntityCloneable[ID, E] with Ordered[E]](entities: Map[ID, E]) =
    new GenericAsyncRepositoryOnMemory(entities)

  /**
   * エクストラクタメソッド。
   *
   * @param repository `org.sisioh.dddbase.lifecycle.memory.async.GenericAsyncRepositoryOnMemory`
   * @tparam ID 識別子の型
   * @tparam E エンティティの型
   * @return 構成要素
   */
  def unapply[ID <: Identifier[_], E <: Entity[ID] with EntityCloneable[ID, E] with Ordered[E]](repository: GenericAsyncRepositoryOnMemory[ID, E]): Option[Map[ID, E]] =
    Some(repository.entities)

}

