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
package org.sisioh.dddbase.core.lifecycle

import scala.language.higherKinds
import org.sisioh.dddbase.core.model.{Entity, Identity}

/**
 * [[org.sisioh.dddbase.core.model.Identity]]を用いて
 * [[org.sisioh.dddbase.core.model.Entity]]
 * を書き込むための責務を表すトレイト。
 *
 * @tparam ID 識別子の型
 * @tparam T エンティティの型
 * @tparam M モナド
 */
trait EntityWriter[+R <: EntityWriter[_, ID, T, M], ID <: Identity[_], T <: Entity[ID], M[+A]]
  extends EntityIO {

  /**
   * エンティティを保存する。
   *
   * @param entity 保存する対象のエンティティ
   * @return Success:
   *         リポジトリインスタンス
   *         Failure
   *         RepositoryExceptionは、リポジトリにアクセスできなかった場合。
   */
  def store(entity: T): M[RepositoryWithEntity[R, T]]

  def update(identity: ID, entity: T) = store(entity)

  def delete(identity: ID): M[R]

  def delete(entity: T): M[R] = delete(entity.identity)

}

