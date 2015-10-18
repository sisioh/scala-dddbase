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
package org.sisioh.dddbase.lifecycle.memory.mutable.async

import org.sisioh.dddbase.core.lifecycle.EntityNotFoundException
import org.sisioh.dddbase.core.lifecycle.async.AsyncResultWithEntity
import org.sisioh.dddbase.core.model.{ EntityCloneable, Entity, Identifier }
import scala.collection.Map
import scala.concurrent._

trait AsyncRepositoryOnMemorySupport[ID <: Identifier[_], E <: Entity[ID] with EntityCloneable[ID, E] with Ordered[E]]
    extends AsyncRepositoryOnMemory[ID, E] {

  protected val _entities: collection.concurrent.Map[ID, E]

  override protected def getEntities: Map[ID, E] = _entities

  override def existBy(identifier: ID)(implicit ctx: Ctx): Future[Boolean] =
    Future.successful(entities.contains(identifier))

  override def resolveBy(identifier: ID)(implicit ctx: Ctx): Future[E] = {
    implicit val executor = getExecutionContext(ctx)
    Future {
      _entities.getOrElse(identifier, throw EntityNotFoundException(Some(s"identifier = $identifier")))
    }
  }

  override def store(entity: E)(implicit ctx: Ctx): Future[Result] = {
    implicit val executor = getExecutionContext(ctx)
    Future {
      _entities.put(entity.identifier, entity)
      AsyncResultWithEntity[This, ID, E](this.asInstanceOf[This], entity)
    }
  }

  override def deleteBy(identifier: ID)(implicit ctx: Ctx): Future[Result] = {
    implicit val executor = getExecutionContext(ctx)
    Future {
      val entity = _entities.remove(identifier).getOrElse(throw EntityNotFoundException(Some(s"identifier = $identifier")))
      AsyncResultWithEntity[This, ID, E](this.asInstanceOf[This], entity)
    }
  }

}
