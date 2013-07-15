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
package org.sisioh.dddbase.core.lifecycle.memory.async

import org.sisioh.dddbase.core.lifecycle.RepositoryWithEntity
import org.sisioh.dddbase.core.lifecycle.async._
import org.sisioh.dddbase.core.model.{Identity, EntityCloneable, Entity}
import scala.concurrent._
import org.sisioh.dddbase.core.lifecycle.memory.sync.SyncRepositoryOnMemory

/**
 * 非同期型オンメモリ不変リポジトリの骨格実装を提供するためのトレイト。
 *
 * `Future` の中で `core` に同期型のオンメモリリポジトリを利用することで非同期版として
 * 実装を提供する。リポジトリの状態変更を起こすメソッドを呼び出した際に、新しいインスタンス
 * を生成するか `this` を返すかは `createInstance` メソッドの振る舞いによって決定する。
 *
 * @tparam SR 内部で利用する同期型リポジトリの型
 * @tparam ID 識別子の型
 * @tparam T エンティティの型
 */
trait AsyncRepositoryOnMemorySupport
[SR <: SyncRepositoryOnMemory[ID, T],
ID <: Identity[_],
T <: Entity[ID] with EntityCloneable[ID, T]]
  extends AsyncRepositoryOnMemory[ID, T] {

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
  protected def createInstance(state: (SR, Option[T])): (R, Option[T])

  override def equals(obj: Any) = obj match {
    case that: AsyncRepositoryOnMemorySupport[_, _, _] =>
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

  def store(entity: T): Future[RepositoryWithEntity[R, T]] = future {
    val result = core.store(entity).get
    val t = (result.repository.asInstanceOf[SR], Some(result.entity))
    val instance = createInstance(t)
    RepositoryWithEntity(instance._1, instance._2.get)
  }

  def delete(identity: ID): Future[R] = future {
    val result = core.delete(identity).get
    val t = (result.asInstanceOf[SR], None)
    val instance = createInstance(t)
    instance._1
  }

}
