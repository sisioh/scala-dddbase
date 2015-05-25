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
package org.sisioh.dddbase.lifecycle.memory.mutable.sync

import org.sisioh.dddbase.core.lifecycle.EntityNotFoundException
import org.sisioh.dddbase.core.lifecycle.sync.SyncResultWithEntity
import org.sisioh.dddbase.core.model.{ EntityCloneable, Entity, Identifier }
import scala.collection.Map
import scala.util.{ Success, Failure, Try }

trait SyncRepositoryOnMemorySupport[ID <: Identifier[_], E <: Entity[ID] with EntityCloneable[ID, E] with Ordered[E]]
    extends SyncRepositoryOnMemory[ID, E] {

  protected val _entities: collection.mutable.Map[ID, E]

  override protected def getEntities: Map[ID, E] = _entities

  override def equals(obj: Any) = obj match {
    case that: SyncRepositoryOnMemorySupport[_, _] =>
      this.entities == that.entities
    case _ => false
  }

  override def hashCode = 31 * entities.##

  override def resolveBy(identifier: ID)(implicit ctx: Ctx) = synchronized {
    existBy(identifier).flatMap {
      _ =>
        Try {
          entities(identifier).clone
        }.recoverWith {
          case ex: NoSuchElementException =>
            Failure(new EntityNotFoundException(Some(s"identifier = $identifier")))
        }
    }
  }

  override def store(entity: E)(implicit ctx: Ctx): Try[Result] = synchronized {
    _entities += (entity.identifier -> entity)
    Success(SyncResultWithEntity(this.asInstanceOf[This], entity))
  }

  override def deleteBy(identifier: ID)(implicit ctx: Ctx): Try[Result] = synchronized {
    resolveBy(identifier).flatMap {
      entity =>
        _entities -= identifier
        Success(SyncResultWithEntity(this.asInstanceOf[This], entity))
    }
  }

  def iterator =
    entities.map(_._2.clone).toSeq.sorted.iterator

}

