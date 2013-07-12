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
package org.sisioh.dddbase.core.lifecycle.memory.sync

import collection.Iterator
import org.sisioh.dddbase.core.lifecycle.EntityNotFoundException
import org.sisioh.dddbase.core.lifecycle.RepositoryWithEntity
import org.sisioh.dddbase.core.lifecycle.sync.SyncRepository
import org.sisioh.dddbase.core.model.{Identity, EntityCloneable, Entity}
import scala.collection.immutable.HashMap
import scala.util.Failure
import scala.util.Success
import scala.util.Try

/**
 * オンメモリで動作する不変リポジトリの実装。
 *
 * @tparam R 当該リポジトリを実装する派生型
 * @tparam ID エンティティの識別子の型
 * @tparam T エンティティの型
 */
trait OnMemorySyncRepositorySupport
[+R <: SyncRepository[_, ID, T],
ID <: Identity[_],
T <: Entity[ID] with EntityCloneable[ID, T] with Ordered[T]]
  extends OnMemorySyncRepository[R, ID, T] {

  /**
   * エンティティを保存するためのマップ。
   */
  protected[core] var entities = new HashMap[ID, T]()

  override def equals(obj: Any) = obj match {
    case that: OnMemorySyncRepositorySupport[_, _, _] =>
      this.entities == that.entities
    case _ => false
  }

  override def hashCode = 31 * entities.hashCode()

  override def clone: R = synchronized {
    val result = super.clone.asInstanceOf[OnMemorySyncRepositorySupport[R, ID, T]]
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


  override def store(entity: T): Try[RepositoryWithEntity[R, T]] = synchronized {
    val result = clone.asInstanceOf[OnMemorySyncRepositorySupport[R, ID, T]]
    result.entities += (entity.identity -> entity)
    Success(RepositoryWithEntity(result.asInstanceOf[R], entity))
  }

  override def delete(identifier: ID): Try[R] = synchronized {
    contains(identifier).flatMap {
      e =>
        if (e) {
          val result = clone.asInstanceOf[OnMemorySyncRepositorySupport[R, ID, T]]
          result.entities -= identifier
          Success(result.asInstanceOf[R])
        } else {
          Failure(new EntityNotFoundException())
        }
    }
  }

  def iterator: Iterator[T] =
    entities.map(_._2.clone).toSeq.sorted.iterator

}
