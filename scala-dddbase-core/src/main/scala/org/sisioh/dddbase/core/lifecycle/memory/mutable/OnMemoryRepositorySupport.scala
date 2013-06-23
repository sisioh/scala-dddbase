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
package org.sisioh.dddbase.core.lifecycle.memory.mutable

import org.sisioh.dddbase.core.lifecycle.memory.OnMemoryRepository
import org.sisioh.dddbase.core.lifecycle.memory.{GenericOnMemoryRepository => GenericOnMemoryImmutableRepository}
import org.sisioh.dddbase.core.lifecycle.{EntityNotFoundException, EntityReaderByOption, Repository}
import org.sisioh.dddbase.core.model.{Identity, EntityCloneable, Entity}
import scala.util.{Success, Try}

/**
 * [[org.sisioh.dddbase.core.lifecycle.memory.mutable.OnMemoryRepositorySupport]]にOption型のサポートを追加するトレイト。
 *
 * @tparam R 当該リポジトリを実装する派生型
 * @tparam ID エンティティの識別子の型
 * @tparam T エンティティの型
 */
trait OnMemoryRepositorySupportByOption
[+R <: Repository[_, ID, T],
ID <: Identity[_],
T <: Entity[ID] with EntityCloneable[ID, T]]
  extends OnMemoryRepositorySupport[R, ID, T] with EntityReaderByOption[ID, T] {

  def resolveOption(identity: ID): Try[Option[T]] = synchronized {
    resolve(identity).map(Some(_)).recoverWith {
      case ex: EntityNotFoundException =>
        Success(None)
    }
  }

}

/**
 * オンメモリで動作する可変リポジトリの実装。
 *
 * @tparam R 当該リポジトリを実装する派生型
 * @tparam ID エンティティの識別子の型
 * @tparam T エンティティの型
 */
trait OnMemoryRepositorySupport
[+R <: Repository[_, ID, T],
ID <: Identity[_],
T <: Entity[ID] with EntityCloneable[ID, T]]
  extends OnMemoryRepository[R, ID, T] {


  /**
   * 内部で利用されるオンメモリリポジトリ
   */
  protected var core: OnMemoryRepository[_, ID, T] =
    new GenericOnMemoryImmutableRepository[ID, T]()

  override def equals(obj: Any) = obj match {
    case that: OnMemoryRepositorySupport[_, _, _] =>
      this.core == that.core
    case _ => false
  }

  override def hashCode = 31 * core.hashCode()

  def store(entity: T): Try[R] = {
    core.store(entity).map {
      result =>
        core = result.asInstanceOf[OnMemoryRepository[_, ID, T]]
        this.asInstanceOf[R]
    }
  }

  def delete(identity: ID): Try[R] = {
    core.delete(identity).map {
      result =>
        core = result.asInstanceOf[OnMemoryRepository[_, ID, T]]
        this.asInstanceOf[R]
    }
  }


  def iterator: Iterator[T] = core.iterator

  def resolve(identity: ID): Try[T] = core.resolve(identity)

}
