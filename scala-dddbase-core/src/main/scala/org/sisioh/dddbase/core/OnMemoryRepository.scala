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

/**
 * オンメモリで動作するリポジトリの実装。
 *
 * @author j5ik2o
 */
@cloneable
class OnMemoryRepository[T <: Entity with EntityCloneable[T]]
  extends Repository[T] with EntityIterableResolver[T] {

  private[core] var entities = collection.mutable.Map.empty[Identifier, T]

  override def equals(obj: Any) = obj match {
    case that: OnMemoryRepository[_] => this.entities == that.entities
    case _ => false
  }

  override def hashCode = entities.hashCode

  override def clone: OnMemoryRepository[T] = {
    val result = super.clone.asInstanceOf[OnMemoryRepository[T]]
    result.entities = result.entities.map(e =>(e._1 -> e._2.clone))
    result
  }

  def resolve(identifier: Identifier) = {
    require(identifier != null)
    if (contains(identifier) == false) {
      throw new EntityNotFoundException()
    }
    entities(identifier).clone
  }

  def store(entity: T) =
    entities += (entity.identifier -> entity)

  def delete(identifier: Identifier) = {
    if (contains(identifier) == false) {
      throw new EntityNotFoundException()
    }
    entities -= identifier
  }

  def delete(entity: T) =
    delete(entity.identifier)

  def iterator: Iterator[T] = entities.map(_._2.clone).iterator
}
