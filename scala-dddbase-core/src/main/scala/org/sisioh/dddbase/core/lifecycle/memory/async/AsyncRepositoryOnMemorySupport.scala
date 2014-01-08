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

import org.sisioh.dddbase.core.model.{Identifier, EntityCloneable, Entity}
import org.sisioh.dddbase.core.lifecycle.forwarding.async.wrapped.AsyncWrappedSyncRepository

/**
 * 非同期型オンメモリ不変リポジトリの骨格実装を提供するためのトレイト。
 *
 * `Future` の中で `core` に同期型のオンメモリリポジトリを利用することで非同期版として
 * 実装を提供する。リポジトリの状態変更を起こすメソッドを呼び出した際に、新しいインスタンス
 * を生成するか `this` を返すかは `createInstance` メソッドの振る舞いによって決定する。
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait AsyncRepositoryOnMemorySupport
[ID <: Identifier[_], E <: Entity[ID] with EntityCloneable[ID, E]]
  extends AsyncRepositoryOnMemory[ID, E] with AsyncWrappedSyncRepository[ID, E] {

  /**
   * 新しい非同期型リポジトリを生成する。
   *
   * @param state 新しい同期型リポジトリ
   * @return 新しい非同期型のリポジトリ
   */
  protected def createInstance(state: (Delegate#This, Option[E])): (This, Option[E])

  override def equals(obj: Any) = obj match {
    case that: AsyncRepositoryOnMemorySupport[_, _] =>
      this.delegate == that.delegate
    case _ => false
  }

  override def hashCode = 31 * delegate.hashCode()

}
