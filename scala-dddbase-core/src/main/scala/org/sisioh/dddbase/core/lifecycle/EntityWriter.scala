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

import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.language.higherKinds

/**
 * [[org.sisioh.dddbase.core.model.Identity]]を用いて
 * [[org.sisioh.dddbase.core.model.Entity]]
 * を書き込むための責務を表すトレイト。
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 * @tparam M モナド
 */
trait EntityWriter[ID <: Identity[_], E <: Entity[ID], M[+A]]
  extends EntityIO {

  type This <: EntityWriter[ID, E, M]

  /**
   * エンティティを保存する。
   *
   * @param entity 保存する対象のエンティティ
   * @return Success:
   *         リポジトリインスタンスと保存されたエンティティ
   *         Failure
   *         RepositoryExceptionは、リポジトリにアクセスできなかった場合。
   */
  def store(entity: E)(implicit ctx: EntityIOContext[M]): M[ResultWithEntity[This, ID, E, M]]

  /**
   * 更新メソッド。
   *
   * {{{
   *   entityWriter(identity) = entity
   * }}}
   *
   * @param identity 識別子
   * @param entity エンティティ
   * @return Success:
   *         リポジトリインスタンスと保存されたエンティティ
   *         Failure
   *         RepositoryExceptionは、リポジトリにアクセスできなかった場合。
   */
  def update(identity: ID, entity: E)(implicit ctx: EntityIOContext[M]) = store(entity)

  /**
   * 指定した識別子のエンティティを削除する。
   *
   * @param identity 識別子
   * @return Success:
   *         リポジトリインスタンスと削除されたエンティティ
   *         Failure:
   *         RepositoryExceptionは、リポジトリにアクセスできなかった場合。
   */
  def delete(identity: ID)(implicit ctx: EntityIOContext[M]): M[ResultWithEntity[This, ID, E, M]]

  /**
   * エンティティを削除する。
   *
   * @param entity エンティティ
   * @return Success:
   *         リポジトリインスタンスと削除されたエンティティ
   *         Failure:
   *         RepositoryExceptionは、リポジトリにアクセスできなかった場合。
   */
  def delete(entity: E)(implicit ctx: EntityIOContext[M]): M[ResultWithEntity[This, ID, E, M]] = delete(entity.identity)

}

