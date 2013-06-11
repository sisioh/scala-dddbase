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
package org.sisioh.dddbase.core

import collection.Iterator
import util.{Try, Success, Failure}
import scala.collection.immutable.HashMap

/**
 * オンメモリで動作する[[org.sisioh.dddbase.core.Repository]]の実装。
 *
 * @tparam ID エンティティの識別子の型
 * @tparam T エンティティの型
 */
class OnMemoryRepository[ID <: Identity[_], T <: Entity[ID] with EntityCloneable[ID, T]]
  extends Repository[ID, T] with EntityIterableReader[ID, T] with Cloneable {

  private[core] var entities = Map.empty[ID, T]

  override def equals(obj: Any) = obj match {
    case that: OnMemoryRepository[_, _] => this.entities == that.entities
    case _ => false
  }

  override def hashCode = entities.hashCode

  override def clone: OnMemoryRepository[ID, T] = {
    val result = super.clone.asInstanceOf[OnMemoryRepository[ID, T]]
    val array = result.entities.toArray
    result.entities = HashMap(array: _*).map(e => (e._1 -> e._2.clone))
    result
  }

  override def resolve(identifier: ID) = synchronized {
    require(identifier != null)
    contains(identifier).flatMap {
      _ =>
        try {
          Success(entities(identifier).clone)
        } catch {
          case ex: NoSuchElementException => Failure(new EntityNotFoundException())
          case ex: Exception => Failure(ex)
        }
    }
  }

  override def resolveOption(identifier: ID) = synchronized {
    require(identifier != null)
    contains(identifier).flatMap {
      _ =>
        try {
          Success(Some(entities(identifier).clone))
        } catch {
          case ex: NoSuchElementException => Success(None)
          case ex: Exception => Failure(ex)
        }
    }
  }

  override def store(entity: T): Try[OnMemoryRepository[ID, T]] = {
    val result = clone
    result.entities += (entity.identity -> entity)
    Success(result)
  }

  override def delete(identifier: ID): Try[OnMemoryRepository[ID, T]] = synchronized {
    contains(identifier).flatMap {
      e =>
        if (e == true) {
          val result = clone
          result.entities -= identifier
          Success(result)
        } else {
          Failure(new EntityNotFoundException())
        }
    }
  }

  def iterator: Iterator[T] = entities.map(_._2.clone).iterator

}
