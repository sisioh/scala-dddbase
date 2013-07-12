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

import org.sisioh.dddbase.core.lifecycle.EntityReaderByOption
import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.util.Try

/**
 * エンティティをOptionでラップして返すための[[org.sisioh.dddbase.core.lifecycle.sync.SyncEntityReader]]。
 *
 * @tparam ID 識別子の型
 * @tparam T エンティティの型
 */
trait SyncEntityReaderByOption[ID <: Identity[_], T <: Entity[ID]]
  extends EntityReaderByOption[ID, T, Try] {
  this: SyncEntityReader[ID, T] =>

  /**
   * 識別子に該当するエンティティを解決する。
   *
   * @param identity 識別子
   * @return Success:
   *         Some: エンティティが存在する場合
   *         None: エンティティが存在しない場合
   *         Failure:
   *         RepositoryExceptionは、リポジトリにアクセスできなかった場合。
   */
  def resolveOption(identity: ID): Try[Option[T]]

}
