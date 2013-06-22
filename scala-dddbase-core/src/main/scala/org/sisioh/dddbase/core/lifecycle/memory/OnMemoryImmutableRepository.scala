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

import collection.Iterator
import org.sisioh.dddbase.core.lifecycle.{Repository, EntityNotFoundException}
import org.sisioh.dddbase.core.model.{Identity, EntityCloneable, Entity}
import scala.Some
import scala.collection.immutable.HashMap
import util.{Try, Success, Failure}

/**
 * オンメモリで動作する不変リポジトリの実装。
 *
 * @tparam R 当該リポジトリを実装する派生型
 * @tparam ID エンティティの識別子の型
 * @tparam T エンティティの型
 */
trait OnMemoryImmutableRepository
[+R <: Repository[_, ID, T],
ID <: Identity[_],
T <: Entity[ID] with EntityCloneable[ID, T]]
  extends OnMemoryRepository[R, ID, T] {

  /**
   * エンティティを保存するためのマップ。
   */
  protected[core] var entities = new HashMap[ID, T]()

  override def equals(obj: Any) = obj match {
    case that: OnMemoryImmutableRepository[_, _, _] =>
      this.entities == that.entities
    case _ => false
  }

  override def hashCode = 31 * entities.hashCode()

  override def clone: R = {
    val result = super.clone.asInstanceOf[OnMemoryImmutableRepository[R, ID, T]]
    val array = result.entities.toArray
    result.entities = HashMap(array: _*).map(e => e._1 -> e._2.clone)
    result.asInstanceOf[R]
  }

  override def resolve(identity: ID) = synchronized {
    contains(identity).flatMap {
      _ =>
        Try {
          entities(identity).clone
        }.recoverWith {
          case ex: NoSuchElementException =>
            Failure(new EntityNotFoundException())
        }
    }
  }

  override def resolveOption(identity: ID) = synchronized {
    contains(identity).flatMap {
      _ =>
        Try {
          Some(entities(identity).clone)
        }.recoverWith {
          case ex: NoSuchElementException =>
            Success(None)
        }
    }
  }

  override def store(entity: T): Try[R] = {
    val result = clone.asInstanceOf[OnMemoryImmutableRepository[R, ID, T]]
    result.entities += (entity.identity -> entity)
    Success(result.asInstanceOf[R])
  }

  override def delete(identifier: ID): Try[R] = synchronized {
    contains(identifier).flatMap {
      e =>
        if (e) {
          val result = clone.asInstanceOf[OnMemoryImmutableRepository[R, ID, T]]
          result.entities -= identifier
          Success(result.asInstanceOf[R])
        } else {
          Failure(new EntityNotFoundException())
        }
    }
  }

  def iterator: Iterator[T] = entities.map(_._2.clone).iterator

}
