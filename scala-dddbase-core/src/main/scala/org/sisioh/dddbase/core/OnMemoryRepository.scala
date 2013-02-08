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
import scalaz._
import Scalaz._

/**
 * オンメモリで動作するリポジトリの実装。
 *
 * @author j5ik2o
 */
class OnMemoryRepository[T <: Entity[ID] with EntityCloneable[T, ID], ID]
  extends Repository[T, ID] with EntityIterableResolver[T, ID] with Cloneable {

  private[core] var entities = Map.empty[Identity[ID], T]

  override def equals(obj: Any) = obj match {
    case that: OnMemoryRepository[_, _] => this.entities == that.entities
    case _ => false
  }

  override def hashCode = entities.hashCode

  override def clone: OnMemoryRepository[T, ID] = {
    val result = super.clone.asInstanceOf[OnMemoryRepository[T, ID]]
    result.entities = result.entities.map(e => (e._1 -> e._2.clone))
    result
  }

  def resolve(identifier: Identity[ID]) = synchronized {
    require(identifier != null)
    if (contains(identifier) == false) {
      throw new EntityNotFoundException()
    }
    entities(identifier).clone
  }

  def resolveOption(identifier: Identity[ID]) = synchronized {
    require(identifier != null)
    if (contains(identifier) == false)
      None
    else
      Some(entities(identifier).clone)
  }

  def store(entity: T) =
    entities += (entity.identity -> entity)

  def delete(identifier: Identity[ID]) = synchronized {
    if (contains(identifier) == false) {
      throw new EntityNotFoundException()
    }
    entities -= identifier
  }

  def delete(entity: T) =
    delete(entity.identity)

  def iterator: Iterator[T] = entities.map(_._2.clone).iterator
}

//
//abstract class AbstractOnMemoryRepository[T <: Entity[ID] with EntityCloneable[T, ID], ID <: java.io.Serializable] {
//
//  implicit def equalEntity: Equal[OnMemoryRepository[T, ID]] = equalA
//
//  implicit def showsEntity: Show[OnMemoryRepository[T, ID]] = shows(_.toString)
//
//}
