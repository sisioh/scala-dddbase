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
package org.sisioh.dddbase.core.lifecycle.memory.mutable.async

import org.sisioh.dddbase.core.lifecycle.memory.mutable.sync.GenericSyncRepositoryOnMemory
import org.sisioh.dddbase.core.model.{Identity, EntityCloneable, Entity}
import scala.concurrent.{Future, ExecutionContext}
import org.sisioh.dddbase.core.lifecycle.forwarding.async.wrapped.AsyncWrappedSyncEntityIOContext
import org.sisioh.dddbase.core.lifecycle.EntityIOContext
import scala.util.Try

/**
 * 汎用的な非同期型オンメモリ可変リポジトリ。
 *
 * @param delegate 内部で利用するオンメモリ可変リポジトリ。
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
class GenericAsyncRepositoryOnMemory[CTX <: EntityIOContext[Future], ID <: Identity[_], E <: Entity[ID] with EntityCloneable[ID, E] with Ordered[E]]
(protected val delegate: GenericSyncRepositoryOnMemory[EntityIOContext[Try], ID, E] = GenericSyncRepositoryOnMemory[EntityIOContext[Try], ID, E]())
  extends AsyncRepositoryOnMemory[CTX, ID, E] {

  type This = GenericAsyncRepositoryOnMemory[CTX, ID, E]

  type Delegate = GenericSyncRepositoryOnMemory[EntityIOContext[Try], ID, E]

}

/**
 * コンパニオンオブジェクト。
 */
object GenericAsyncRepositoryOnMemory {

  object Implicits {

    import scala.concurrent.ExecutionContext.Implicits.global

    implicit val defaultEntityIOContext = createEntityIOContext

  }

  /**
   * [[org.sisioh.dddbase.core.lifecycle.EntityIOContext]]を生成する。
   *
   * @param executor `ExecutionContext`
   * @return [[org.sisioh.dddbase.core.lifecycle.forwarding.async.wrapped.AsyncWrappedSyncEntityIOContext]]
   */
  def createEntityIOContext(implicit executor: ExecutionContext) = AsyncWrappedSyncEntityIOContext()

  /**
   * ファクトリメソッド。
   *
   * @param delegate 内部で利用するオンメモリ可変リポジトリ。
   * @tparam ID 識別子の型
   * @tparam T エンティティの型
   * @return [[org.sisioh.dddbase.core.lifecycle.memory.mutable.async.GenericAsyncRepositoryOnMemory]]
   */
  def apply[CTX <: EntityIOContext[Future], ID <: Identity[_], T <: Entity[ID] with EntityCloneable[ID, T] with Ordered[T]]
  (delegate: GenericSyncRepositoryOnMemory[EntityIOContext[Try], ID, T] = GenericSyncRepositoryOnMemory[EntityIOContext[Try], ID, T]()) =
    new GenericAsyncRepositoryOnMemory[CTX, ID, T](delegate)

  /**
   * エクストラクタメソッド。
   *
   * @param repository [[org.sisioh.dddbase.core.lifecycle.memory.mutable.async.GenericAsyncRepositoryOnMemory]]
   * @tparam ID 識別子の型
   * @tparam T エンティティの型
   * @return 構成要素
   */
  def unapply[CTX <: EntityIOContext[Future], ID <: Identity[_], T <: Entity[ID] with EntityCloneable[ID, T] with Ordered[T]]
  (repository: GenericAsyncRepositoryOnMemory[CTX, ID, T]): Option[GenericSyncRepositoryOnMemory[EntityIOContext[Try], ID, T]] =
    Some(repository.delegate)

}

