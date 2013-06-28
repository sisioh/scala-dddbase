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
package org.sisioh.dddbase.core.lifecycle.memory

import org.sisioh.dddbase.core.model.{Identity, EntityCloneable, Entity}

/**
 * 汎用的な非同期型オンメモリ不変リポジトリ。
 *
 * @param core 内部で利用するオンメモリ不変リポジトリ。
 * @tparam ID 識別子の型
 * @tparam T エンティティの型
 */
class GenericAsyncOnMemoryRepository[ID <: Identity[_], T <: Entity[ID] with EntityCloneable[ID, T] with Ordered[T]]
(protected val core: GenericOnMemoryRepository[ID, T] = GenericOnMemoryRepository[ID, T]())
  extends AsyncOnMemoryRepositorySupport[GenericAsyncOnMemoryRepository[ID, T], GenericOnMemoryRepository[ID, T], ID, T] {

  protected def createInstance(state: GenericOnMemoryRepository[ID, T]): GenericAsyncOnMemoryRepository[ID, T] =
    new GenericAsyncOnMemoryRepository[ID, T](state)

}

/**
 * コンパニオンオブジェクト。
 */
object GenericAsyncOnMemoryRepository {

  /**
   * ファクトリメソッド。
   *
   * @param core 内部で利用するオンメモリ不変リポジトリ
   * @tparam ID 識別子の型
   * @tparam T エンティティの型
   * @return [[org.sisioh.dddbase.core.lifecycle.memory.GenericAsyncOnMemoryRepository]]
   */
  def apply[ID <: Identity[_], T <: Entity[ID] with EntityCloneable[ID, T] with Ordered[T]]
  (core: GenericOnMemoryRepository[ID, T] = GenericOnMemoryRepository[ID, T]()) =
    new GenericAsyncOnMemoryRepository(core)

  /**
   * エクストラクタメソッド。
   *
   * @param repository [[org.sisioh.dddbase.core.lifecycle.memory.GenericAsyncOnMemoryRepository]]
   * @tparam ID 識別子の型
   * @tparam T エンティティの型
   * @return 構成要素
   */
  def unapply[ID <: Identity[_], T <: Entity[ID] with EntityCloneable[ID, T] with Ordered[T]]
  (repository: GenericAsyncOnMemoryRepository[ID, T]) : Option[GenericOnMemoryRepository[ID, T]] =
    Some(repository.core)

}


