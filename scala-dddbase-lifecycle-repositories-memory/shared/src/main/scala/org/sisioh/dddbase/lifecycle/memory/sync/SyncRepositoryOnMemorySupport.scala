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
package org.sisioh.dddbase.lifecycle.memory.sync

import collection.Iterator
import org.sisioh.dddbase.core.lifecycle.EntityNotFoundException
import org.sisioh.dddbase.core.lifecycle.sync.SyncResultWithEntity
import org.sisioh.dddbase.core.model.{ Identifier, EntityCloneable, Entity }
import scala.util.Failure
import scala.util.Success
import scala.util.Try

/**
 * オンメモリで動作する不変リポジトリの実装。
 *
 * @tparam ID エンティティの識別子の型
 * @tparam E エンティティの型
 */
trait SyncRepositoryOnMemorySupport[ID <: Identifier[_], E <: Entity[ID] with Ordered[E]]
    extends SyncRepositoryOnMemory[ID, E] {

  protected def createInstance(entities: Map[ID, E]): This

  override def equals(obj: Any) = obj match {
    case that: SyncRepositoryOnMemorySupport[_, _] =>
      this.entities == that.entities
    case _ => false
  }

  override def hashCode = 31 * entities.hashCode()

  override def resolveBy(identifier: ID)(implicit ctx: Ctx) = synchronized {
    existBy(identifier).flatMap {
      _ =>
        Try {
          entities(identifier)
        }.recoverWith {
          case ex: NoSuchElementException =>
            Failure(new EntityNotFoundException(Some(s"identifier = $identifier")))
        }
    }
  }

  override def store(entity: E)(implicit ctx: Ctx): Try[Result] = synchronized {
    val result = createInstance(entities + (entity.identifier -> entity))
    Success(SyncResultWithEntity(result.asInstanceOf[This], entity))
  }

  override def deleteBy(identifier: ID)(implicit ctx: Ctx): Try[Result] = synchronized {
    resolveBy(identifier).flatMap {
      entity =>
        val result = createInstance(entities - identifier)
        Success(SyncResultWithEntity(result.asInstanceOf[This], entity))
    }
  }

  def iterator: Iterator[E] =
    entities.values.toSeq.sorted.iterator

}
