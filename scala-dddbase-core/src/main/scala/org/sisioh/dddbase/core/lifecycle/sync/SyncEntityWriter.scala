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
package org.sisioh.dddbase.core.lifecycle.sync

import org.sisioh.dddbase.core.lifecycle.EntityWriter
import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.util.Try

/**
 * [[org.sisioh.dddbase.core.model.Identity]]を用いて
 * [[org.sisioh.dddbase.core.model.Entity]]
 * を書き込むための責務を表すインターフェイス。
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait SyncEntityWriter[ID <: Identity[_], E <: Entity[ID]]
  extends EntityWriter[ID, E, Try] {

  type This <: SyncEntityWriter[ID, E]
  type Result = SyncResultWithEntity[This, ID, E]
  type Results = SyncResultWithEntities[This, ID, E]


  protected def traverseWithThis[A](values: Seq[A])(processor: (This, A) => Try[Result])(implicit ctx: Ctx): Try[Results] = Try {
    val result = values.foldLeft[(This, Seq[E])]((this.asInstanceOf[This], Seq.empty[E])) {
      (resultWithEntities, task) =>
        val resultWithEntity = processor(resultWithEntities._1, task).get
        (resultWithEntity.result.asInstanceOf[This], resultWithEntities._2 :+ resultWithEntity.entity)
    }
    SyncResultWithEntities(result._1, result._2)
  }

  def multiStore(entities: E*)(implicit ctx: Ctx): Try[Results] =
    traverseWithThis(entities) {
      (repository, entity) =>
        repository.multiStore(entity).asInstanceOf[Try[Result]]
    }

  def multiDeleteBy(identifiers: ID*)(implicit ctx: Ctx): Try[Results] =
    traverseWithThis(identifiers) {
      (repository, identifier) =>
        repository.multiDeleteBy(identifier).asInstanceOf[Try[Result]]
    }

}
