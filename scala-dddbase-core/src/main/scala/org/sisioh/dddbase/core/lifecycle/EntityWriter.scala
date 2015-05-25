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

import org.sisioh.dddbase.core.model.{ Entity, Identifier }

import scala.language.higherKinds

/**
 * `Identifier`を用いて`Entity`を書き込むための責務を表すトレイト。
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 * @tparam M モナド
 */
trait EntityWriter[ID <: Identifier[_], E <: Entity[ID], M[+_]]
    extends EntityIO[M] {

  type This <: EntityWriter[ID, E, M]
  type Result <: ResultWithEntity[This, ID, E, M]
  type Results <: ResultWithEntities[This, ID, E, M]

  protected def traverseWithThis[A](values: Seq[A])(processor: (This, A) => M[Result])(implicit ctx: Ctx): M[Results]

  /**
   * エンティティを保存する。
   *
   * @param entity 保存する対象のエンティティ
   * @return Success:
   *         リポジトリインスタンスと保存されたエンティティ
   *         Failure
   *         RepositoryExceptionは、リポジトリにアクセスできなかった場合。
   */
  def store(entity: E)(implicit ctx: Ctx): M[Result]

  /**
   * 複数のエンティティを保存する。
   *
   * @param entities 保存する対象のエンティティ
   * @return Success:
   *         リポジトリインスタンスと保存されたエンティティ
   *         Failure
   *         RepositoryExceptionは、リポジトリにアクセスできなかった場合。
   */
  def storeMulti(entities: E*)(implicit ctx: Ctx): M[Results] =
    traverseWithThis(entities) {
      (repository, entity) =>
        repository.store(entity).asInstanceOf[M[Result]]
    }

  /**
   * 更新メソッド。
   *
   * {{{
   *   entityWriter(identifier) = entity
   * }}}
   *
   * @param identifier 識別子
   * @param entity エンティティ
   * @return Success:
   *         リポジトリインスタンスと保存されたエンティティ
   *         Failure
   *         RepositoryExceptionは、リポジトリにアクセスできなかった場合。
   */
  def update(identifier: ID, entity: E)(implicit ctx: Ctx) = store(entity)

  /**
   * 指定した識別子のエンティティを削除する。
   *
   * @param identifier 識別子
   * @return Success:
   *         リポジトリインスタンスと削除されたエンティティ
   *         Failure:
   *         RepositoryExceptionは、リポジトリにアクセスできなかった場合。
   */
  def deleteBy(identifier: ID)(implicit ctx: Ctx): M[Result]

  /**
   * 指定した複数の識別子のエンティティを削除する。
   *
   * @param identifiers 識別子
   * @return Success:
   *         リポジトリインスタンスと削除されたエンティティ
   *         Failure:
   *         RepositoryExceptionは、リポジトリにアクセスできなかった場合。
   */
  def deleteByMulti(identifiers: ID*)(implicit ctx: Ctx): M[Results] =
    traverseWithThis(identifiers) {
      (repository, identifier) =>
        repository.deleteBy(identifier).asInstanceOf[M[Result]]
    }

}

