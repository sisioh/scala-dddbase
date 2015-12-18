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
package org.sisioh.dddbase.core.lifecycle.async

import org.sisioh.dddbase.core.lifecycle.EntityWriter
import org.sisioh.dddbase.core.model.{ Entity, Identifier }
import scala.concurrent._

/**
 * 非同期版`org.sisioh.dddbase.core.lifecycle.EntityWriter`。
 *
 * @see `org.sisioh.dddbase.core.lifecycle.EntityWriter`
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait AsyncEntityWriter[ID <: Identifier[_], E <: Entity[ID]]
    extends AsyncEntityIO with EntityWriter[ID, E, Future] {

  type This <: AsyncEntityWriter[ID, E]
  type Result = AsyncResultWithEntity[This, ID, E]
  type Results = AsyncResultWithEntities[This, ID, E]

  protected final def traverseWithThis[A](values: Seq[A])(processor: (This, A) => Future[Result])(implicit ctx: Ctx): Future[Results] = {
    implicit val executor = getExecutionContext(ctx)
    values.foldLeft(Future.successful(AsyncResultWithEntities[This, ID, E](this.asInstanceOf[This], Seq.empty[E]))) {
      case (future, value) =>
        for {
          AsyncResultWithEntities(repo, entities) <- future
          AsyncResultWithEntity(r, e) <- processor(repo, value)
        } yield {
          AsyncResultWithEntities(r, entities :+ e)
        }
    }
  }

}
