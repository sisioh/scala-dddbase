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

import org.sisioh.dddbase.core.lifecycle._
import org.sisioh.dddbase.core.model.{Identity, EntityCloneable, Entity}
import scala.concurrent._
import org.sisioh.dddbase.core.lifecycle.RepositoryWithEntity
import org.sisioh.dddbase.core.lifecycle.EntitiesChunk
import scala.Some

/**
 * [[org.sisioh.dddbase.core.lifecycle.memory.AsyncOnMemoryRepositorySupport]]にOption型のサポートを追加するトレイト。
 *
 * @tparam AR 当該リポジトリを実装する派生型
 * @tparam SR 内部で利用する同期型リポジトリの型
 * @tparam ID 識別子の型
 * @tparam T エンティティの型
 */
trait AsyncOnMemoryRepositorySupportByOption
[+AR <: AsyncRepository[_, ID, T],
SR <: OnMemorySyncRepository[_, ID, T] with SyncEntityReaderByOption[ID, T],
ID <: Identity[_],
T <: Entity[ID] with EntityCloneable[ID, T]]
  extends AsyncOnMemoryRepositorySupport[AR, SR, ID, T] with AsyncEntityReaderByOption[ID, T] {

  def resolveOption(identifier: ID)(implicit executor: ExecutionContext) = future {
    core.resolveOption(identifier).get
  }

}

/**
 * [[org.sisioh.dddbase.core.lifecycle.memory.AsyncOnMemoryRepositorySupport]]に全件取得のための機能を追加するトレイト。
 *
 * @tparam AR 当該リポジトリを実装する派生型
 * @tparam SR 内部で利用する同期型リポジトリの型
 * @tparam ID 識別子の型
 * @tparam T エンティティの型
 */
trait AsyncOnMemoryRepositorySupportBySeq
[+AR <: AsyncRepository[_, ID, T],
SR <: OnMemorySyncRepository[_, ID, T] with SyncEntityReaderByOption[ID, T],
ID <: Identity[_],
T <: Entity[ID] with EntityCloneable[ID, T]]
  extends AsyncOnMemoryRepositorySupport[AR, SR, ID, T] with AsyncEntityReaderBySeq[ID, T] {

  def resolveAll(implicit executor: ExecutionContext): Future[Seq[T]] = future {
    core.toSeq.map(_.asInstanceOf[T])
  }

}

/**
 * [[org.sisioh.dddbase.core.lifecycle.memory.AsyncOnMemoryRepositorySupport]]に
 * [[org.sisioh.dddbase.core.lifecycle.AsyncEntityReaderByPredicate]]ための機能を追加するトレイト。
 *
 * @tparam AR 当該リポジトリを実装する派生型
 * @tparam SR 内部で利用する同期型リポジトリの型
 * @tparam ID 識別子の型
 * @tparam T エンティティの型
 */
trait AsyncOnMemoryRepositorySupportByPredicate
[+AR <: AsyncRepository[_, ID, T],
SR <: OnMemorySyncRepository[_, ID, T] with SyncEntityReaderByPredicate[ID, T],
ID <: Identity[_],
T <: Entity[ID] with EntityCloneable[ID, T]]
  extends AsyncOnMemoryRepositorySupport[AR, SR, ID, T]
  with AsyncEntityReaderByPredicate[ID, T] {

  def filterByPredicate
  (predicate: (T) => Boolean, index: Option[Int], maxEntities: Option[Int])
  (implicit executor: ExecutionContext): Future[EntitiesChunk[ID, T]] = future {
    core.filterByPredicate(predicate, index, maxEntities).get
  }

}

/**
 * [[org.sisioh.dddbase.core.lifecycle.memory.AsyncOnMemoryRepositorySupport]]に
 * [[org.sisioh.dddbase.core.lifecycle.EntitiesChunk]]のための機能を追加するトレイト。
 *
 * @tparam AR 当該リポジトリを実装する派生型
 * @tparam SR 内部で利用する同期型リポジトリの型
 * @tparam ID 識別子の型
 * @tparam T エンティティの型
 */
trait AsyncOnMemoryRepositorySupportByChunk
[+AR <: AsyncRepository[_, ID, T],
SR <: OnMemorySyncRepository[_, ID, T] with SyncEntityReaderByChunk[ID, T],
ID <: Identity[_],
T <: Entity[ID] with EntityCloneable[ID, T]]
  extends AsyncOnMemoryRepositorySupport[AR, SR, ID, T] with AsyncEntityReaderByChunk[ID, T] {

  def resolveChunk(index: Int, maxEntities: Int)(implicit executor: ExecutionContext): Future[EntitiesChunk[ID, T]] = future {
    core.resolveChunk(index, maxEntities).get
  }

}

/**
 * 非同期型オンメモリ不変リポジトリの骨格実装を提供するためのトレイト。
 *
 * `Future` の中で `core` に同期型のオンメモリリポジトリを利用することで非同期版として
 * 実装を提供する。リポジトリの状態変更を起こすメソッドを呼び出した際に、新しいインスタンス
 * を生成するか `this` を返すかは `createInstance` メソッドの振る舞いによって決定する。
 *
 * @tparam AR 当該リポジトリを実装する派生型
 * @tparam SR 内部で利用する同期型リポジトリの型
 * @tparam ID 識別子の型
 * @tparam T エンティティの型
 */
trait AsyncOnMemoryRepositorySupport
[+AR <: AsyncRepository[_, ID, T],
SR <: OnMemorySyncRepository[_, ID, T],
ID <: Identity[_],
T <: Entity[ID] with EntityCloneable[ID, T]]
  extends AsyncOnMemoryRepository[AR, ID, T] {

  /**
   * 内部で利用する同期型リポジトリ。
   */
  protected val core: SR

  /**
   * 新しい非同期型リポジトリを生成する。
   *
   * @param state 新しい同期型リポジトリ
   * @return 新しい非同期型のリポジトリ
   */
  protected def createInstance(state: (SR, Option[T])): (AR, Option[T])

  override def equals(obj: Any) = obj match {
    case that: AsyncOnMemoryRepositorySupport[_, _, _, _] =>
      this.core == that.core
    case _ => false
  }

  override def hashCode = 31 * core.hashCode()

  def resolve(identifier: ID) = future {
    core.resolve(identifier).get
  }

  def contains(identifier: ID) = future {
    core.contains(identifier).get
  }

  def store(entity: T): Future[RepositoryWithEntity[AR, T]] = future {
    val result = core.store(entity).get
    val t = (result.repository.asInstanceOf[SR], Some(result.entity))
    val instance  = createInstance(t)
    RepositoryWithEntity(instance._1, instance._2.get)
  }

  def delete(identity: ID): Future[AR] = future {
    val result = core.delete(identity).get
    val t = (result.asInstanceOf[SR], None)
    val instance = createInstance(t)
    instance._1
  }

}
