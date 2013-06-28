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
SR <: OnMemoryRepository[_, ID, T] with EntityReaderByOption[ID, T],
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
SR <: OnMemoryRepository[_, ID, T] with EntityReaderByOption[ID, T],
ID <: Identity[_],
T <: Entity[ID] with EntityCloneable[ID, T]]
  extends AsyncOnMemoryRepositorySupport[AR, SR, ID, T] with AsyncEntityReaderBySeq[ID, T] {

  def resolveAll(implicit executor: ExecutionContext): Future[Seq[T]] = future {
    core.toSeq.map(_.asInstanceOf[T])
  }

}

/**
 * [[org.sisioh.dddbase.core.lifecycle.memory.AsyncOnMemoryRepositorySupport]]に[[org.sisioh.dddbase.core.lifecycle.EntitiesChunk]]のための機能を追加するトレイト。
 *
 * @tparam AR 当該リポジトリを実装する派生型
 * @tparam SR 内部で利用する同期型リポジトリの型
 * @tparam ID 識別子の型
 * @tparam T エンティティの型
 */
trait AsyncOnMemoryRepositorySupportByChunk
[+AR <: AsyncRepository[_, ID, T],
SR <: OnMemoryRepository[_, ID, T] with EntityReaderByChunk[ID, T],
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
SR <: OnMemoryRepository[_, ID, T],
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
  protected def createInstance(state: SR): AR

  override def equals(obj: Any) = obj match {
    case that: AsyncOnMemoryRepositorySupport[_, _, _, _] =>
      this.core == that.core
    case _ => false
  }

  override def hashCode = 31 * core.hashCode()

  def resolve(identifier: ID)(implicit executor: ExecutionContext) = future {
    core.resolve(identifier).get
  }

  def contains(identifier: ID)(implicit executor: ExecutionContext) = future {
    core.contains(identifier).get
  }

  def store(entity: T)(implicit executor: ExecutionContext): Future[AR] = future {
    createInstance(core.store(entity).get.asInstanceOf[SR])
  }

  def delete(identity: ID)(implicit executor: ExecutionContext): Future[AR] = future {
    createInstance(core.delete(identity).get.asInstanceOf[SR])
  }

}
