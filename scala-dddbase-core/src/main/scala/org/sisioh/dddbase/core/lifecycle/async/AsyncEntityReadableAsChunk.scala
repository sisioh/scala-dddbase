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

import org.sisioh.dddbase.core.lifecycle.{ EntityIOContext, EntityReadableAsChunk, EntitiesChunk }
import org.sisioh.dddbase.core.model.{ Entity, Identifier }
import scala.concurrent.Future

/**
 * 非同期版`org.sisioh.dddbase.core.lifecycle.EntityReadableAsChunk`。
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait AsyncEntityReadableAsChunk[ID <: Identifier[_], E <: Entity[ID]]
    extends EntityReadableAsChunk[ID, E, Future] {
  this: AsyncEntityReader[ID, E] =>

  /**
   * @return Success:
   *         チャンク
   *         Failure:
   *         RepositoryExceptionは、リポジトリにアクセスできなかった場合。
   */
  def resolveAsChunk(index: Int, maxEntities: Int)(implicit ctx: EntityIOContext[Future]): Future[EntitiesChunk[ID, E]]

}
