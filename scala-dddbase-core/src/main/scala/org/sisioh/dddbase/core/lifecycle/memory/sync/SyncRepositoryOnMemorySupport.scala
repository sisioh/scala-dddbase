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
import org.sisioh.dddbase.core.lifecycle.{EntityIOContext, EntityNotFoundException}
import org.sisioh.dddbase.core.lifecycle.sync.SyncResultWithEntity
import org.sisioh.dddbase.core.model.{Identity, EntityCloneable, Entity}
import scala.collection.immutable.HashMap
import scala.util.Failure
import scala.util.Success
import scala.util.Try

/**
 * オンメモリで動作する不変リポジトリの実装。
 *
 * @tparam ID エンティティの識別子の型
 * @tparam E エンティティの型
 */
trait SyncRepositoryOnMemorySupport
[ID <: Identity[_],
E <: Entity[ID] with EntityCloneable[ID, E] with Ordered[E]]
  extends SyncRepositoryOnMemory[ID, E] {

  /**
   * エンティティを保存するためのマップ。
   */
  protected[core] var entities = new HashMap[ID, E]()

  override def equals(obj: Any) = obj match {
    case that: SyncRepositoryOnMemorySupport[_, _] =>
      this.entities == that.entities
    case _ => false
  }

  override def hashCode = 31 * entities.hashCode()

  override def clone: This = synchronized {
    val result = super.clone.asInstanceOf[SyncRepositoryOnMemorySupport[ID, E]]
    val array = result.entities.toArray
    result.entities = HashMap(array: _*).map(e => e._1 -> e._2.clone)
    result.asInstanceOf[This]
  }

  override def resolve(identity: ID)(implicit ctx: EntityIOContext[Try]) = synchronized {
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


  override def store(entity: E)(implicit ctx: EntityIOContext[Try]): Try[SyncResultWithEntity[This, ID, E]] = synchronized {
    val result = clone.asInstanceOf[SyncRepositoryOnMemorySupport[ID, E]]
    result.entities += (entity.identity -> entity)
    Success(SyncResultWithEntity(result.asInstanceOf[This], entity))
  }

  override def delete(identifier: ID)(implicit ctx: EntityIOContext[Try]): Try[SyncResultWithEntity[This, ID, E]] = synchronized {
    resolve(identifier).flatMap {
      entity =>
        val result = clone.asInstanceOf[SyncRepositoryOnMemorySupport[ID, E]]
        result.entities -= identifier
        Success(SyncResultWithEntity(result.asInstanceOf[This], entity))
    }
  }

  def iterator: Iterator[E] =
    entities.map(_._2.clone).toSeq.sorted.iterator

}
