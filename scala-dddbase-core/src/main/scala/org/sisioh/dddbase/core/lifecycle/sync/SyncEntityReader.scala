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
package org.sisioh.dddbase.core.lifecycle.sync

import org.sisioh.dddbase.core.lifecycle.{EntityIOContext, EntityReader}
import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.util.Try

/**
 * 同期的に読み込むための[[org.sisioh.dddbase.core.lifecycle.EntityReader]]
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait SyncEntityReader[CTX <: EntityIOContext[Try], ID <: Identity[_], E <: Entity[ID]]
  extends EntityReader[CTX, ID, E, Try] {

  /**
   * @return Success: エンティティ
   *         Failure: EntityNotFoundExceptionは、エンティティが見つからなかった場合、
   *         RepositoryExceptionは、リポジトリにアクセスできなかった場合。
   */
  def resolve(identity: ID)(implicit ctx: CTX): Try[E]

  def resolves(identities: Seq[ID])(implicit ctx: CTX): Try[Seq[E]] = Try {
    identities.map(resolve(_).get)
  }

  /**
   * @return Success: 存在する場合はtrue
   *         Failure: RepositoryExceptionは、リポジトリにアクセスできなかった場合。
   */
  def containsByIdentity(identity: ID)(implicit ctx: CTX): Try[Boolean]

  def containsByIdentities(identities: Seq[ID])(implicit ctx: CTX): Try[Boolean] = Try {
    identities.map(containsByIdentity(_).get).forall(_ == true)
  }

}
