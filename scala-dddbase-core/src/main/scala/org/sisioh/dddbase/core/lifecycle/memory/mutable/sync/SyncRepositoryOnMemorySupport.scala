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
package org.sisioh.dddbase.core.lifecycle.memory.mutable.sync

import org.sisioh.dddbase.core.lifecycle.RepositoryWithEntity
import org.sisioh.dddbase.core.lifecycle.sync._
import org.sisioh.dddbase.core.model.{Identity, EntityCloneable, Entity}
import scala.util.Try
import org.sisioh.dddbase.core.lifecycle.memory.sync.SyncRepositoryOnMemory


/**
 * オンメモリで動作する可変リポジトリの実装。
 *
 * @tparam ID エンティティの識別子の型
 * @tparam T エンティティの型
 */
trait SyncRepositoryOnMemorySupport
[ID <: Identity[_],
T <: Entity[ID] with EntityCloneable[ID, T] with Ordered[T]]
  extends SyncRepositoryOnMemory[ID, T] {

  /**
   * 内部で利用されるオンメモリリポジトリ
   */
  protected var core: SyncRepositoryOnMemory[ID, T] =
    new org.sisioh.dddbase.core.lifecycle.memory.sync.GenericSyncRepositoryOnMemory[ID, T]()

  override def equals(obj: Any) = obj match {
    case that: SyncRepositoryOnMemorySupport[_, _] =>
      this.core == that.core
    case _ => false
  }

  override def hashCode = 31 * core.##

  def store(entity: T): Try[RepositoryWithEntity[This, T]] = {
    core.store(entity).map {
      result =>
        core = result.repository.asInstanceOf[SyncRepositoryOnMemory[ID, T]]
        RepositoryWithEntity(this.asInstanceOf[This], result.entity)
    }
  }

  def delete(identity: ID): Try[This] = {
    core.delete(identity).map {
      result =>
        core = result.asInstanceOf[SyncRepositoryOnMemory[ID, T]]
        this.asInstanceOf[This]
    }
  }

  def iterator: Iterator[T] = core.iterator

  def resolve(identity: ID): Try[T] = core.resolve(identity)

}
