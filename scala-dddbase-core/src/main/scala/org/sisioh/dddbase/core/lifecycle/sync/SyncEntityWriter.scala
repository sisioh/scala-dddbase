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

import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.util.Try
import org.sisioh.dddbase.core.lifecycle.{RepositoryWithEntity, EntityWriter}

/**
 * [[org.sisioh.dddbase.core.model.Identity]]を用いて
 * [[org.sisioh.dddbase.core.model.Entity]]
 * を書き込むための責務を表すインターフェイス。
 *
 * @tparam ID 識別子の型
 * @tparam T エンティティの型
 */
trait SyncEntityWriter[ID <: Identity[_], T <: Entity[ID]]
  extends EntityWriter[ID, T, Try] {

  type R <: SyncEntityWriter[ID, T]

  /**
   * エンティティを保存する。
   *
   * @param entity 保存する対象のエンティティ
   * @return Success:
   *         リポジトリインスタンス
   *         Failure
   *         RepositoryExceptionは、リポジトリにアクセスできなかった場合。
   */
  def store(entity: T): Try[RepositoryWithEntity[R, T]]

  /**
   * 指定した識別子のエンティティを削除する。
   *
   * @param identity 識別子
   * @return Success:
   *         リポジトリインスタンス
   *         Failure:
   *         RepositoryExceptionは、リポジトリにアクセスできなかった場合。
   */
  def delete(identity: ID): Try[R]

}
