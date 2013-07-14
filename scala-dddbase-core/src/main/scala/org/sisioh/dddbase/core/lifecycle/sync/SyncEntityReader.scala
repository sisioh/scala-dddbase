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

import org.sisioh.dddbase.core.lifecycle.EntityReader
import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.util.Try

/**
 * [[org.sisioh.dddbase.core.model.Identity]]を用いて、[[org.sisioh.dddbase.core.model.Entity]]
 * を読み込むための責務を表すインターフェイス。
 *
 * @tparam ID 識別子の型
 * @tparam T エンティティの型
 */
trait SyncEntityReader[ID <: Identity[_], T <: Entity[ID]]
  extends EntityReader[ID, T, Try] {

  /**
   * 識別子に該当するエンティティを解決する。
   *
   * @param identity 識別子
   * @return Success: エンティティ
   *         Failure: EntityNotFoundExceptionは、エンティティが見つからなかった場合、
   *         RepositoryExceptionは、リポジトリにアクセスできなかった場合。
   */
  def resolve(identity: ID): Try[T]

  /**
   * 指定した識別子のエンティティが存在するかを返す。
   *
   * @param identity 識別子
   * @return Success: 存在する場合はtrue
   *         Failure: RepositoryExceptionは、リポジトリにアクセスできなかった場合。
   */
  def contains(identity: ID): Try[Boolean]

}
