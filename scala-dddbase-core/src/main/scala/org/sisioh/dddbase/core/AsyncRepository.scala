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
package org.sisioh.dddbase.core

import scala.concurrent._

/**
 * 非同期版[[org.sisioh.dddbase.core.EntityResolver]]。
 *
 * @see [[org.sisioh.dddbase.core.EntityResolver]]
 *
 * @tparam ID 識別子の型
 * @tparam T エンティティの型
 */
trait AsyncEntityResolver[ID <: Identity[_], T <: Entity[ID]] {

  /**
   * 識別子に該当するエンティティを解決する。
   *
   * @see [[org.sisioh.dddbase.core.EntityResolver.resolve()]]
   *
   * @param identity 識別子
   * @return Success:
   *          非同期リポジトリ
   *         Failure:
   *          EntityNotFoundException リポジトリにアクセスできなかった場合
   *          RepositoryException リポジトリにアクセスできなかった場合
   */
  def resolve(identity: ID): Future[T]

  /**
   * 識別子に該当するエンティティを解決する。
   *
   * @see [[org.sisioh.dddbase.core.EntityResolver.resolve()]]
   *
   * @param identity 識別子
   * @return Success:
   *          Option[エンティティ]
   *         Failure:
   *          Futureが失敗した場合の例外
   */
  def resolveOption(identity: ID): Future[Option[T]]

  /**
   * [[org.sisioh.dddbase.core.AsyncEntityResolver.resolve()]]へのショートカット。
   *
   * @param identity 識別子
   * @return Success:
   *          非同期リポジトリ
   *         Failure:
   *          EntityNotFoundException リポジトリにアクセスできなかった場合
   *          RepositoryException リポジトリにアクセスできなかった場合
   */
  def apply(identity: ID) = resolve(identity)

  /**
   * 指定した識別子のエンティティが存在するかを返す。
   *
   * @param identity 識別子
   * @return Success:
   *          存在する場合はtrue
   *         Failure:
   *          EntityNotFoundException リポジトリにアクセスできなかった場合
   *          RepositoryException リポジトリにアクセスできなかった場合
   *          Futureが失敗した場合の例外
   */
  def contains(identity: ID): Future[Boolean]

  /**
   * 指定したエンティティが存在するかを返す。
   *
   * @param entity エンティティ
   * @return Success:
   *          存在する場合はtrue
   *         Failure:
   *          EntityNotFoundException リポジトリにアクセスできなかった場合
   *          RepositoryException リポジトリにアクセスできなかった場合
   *          Futureが失敗した場合の例外
   */
  def contains(entity: T): Future[Boolean] = contains(entity.identity)

}

/**
 * 非同期版[[org.sisioh.dddbase.core.Repository]]。
 *
 * @see [[org.sisioh.dddbase.core.Repository]]
 *
 * @tparam ID 識別子の型
 * @tparam T エンティティの型
 */
trait AsyncRepository[ID <: Identity[_], T <: Entity[ID]] extends AsyncEntityResolver[ID, T] {

  /**
   * エンティティを保存する。
   *
   * @see [[org.sisioh.dddbase.core.Repository.store( )]]
   *
   * @param entity 保存する対象のエンティティ
   * @return Success:
   *          非同期リポジトリ
   *         Failure:
   *          RepositoryException リポジトリにアクセスできなかった場合
   *          Futureが失敗した場合の例外
   */
  def store(entity: T): Future[AsyncRepository[ID, T]]

  /**
   * [[org.sisioh.dddbase.core.AsyncRepository.store()]]へのショートカット。
   *
   * @param identifier 識別子
   * @param entity 保存する対象のエンティティ
   * @return Success:
   *          非同期リポジトリ
   *         Failure:
   *          RepositoryException リポジトリにアクセスできなかった場合
   *          Futureが失敗した場合の例外
   */
  def update(identifier: ID, entity: T) = store(entity)

  /**
   * deleteメソッドの非同期版
   *
   * @see [[org.sisioh.dddbase.core.Repository.delete()]]
   *
   * @param identity 識別子
   * @return Success:
   *          非同期リポジトリ
   *         Failure:
   *          RepositoryException リポジトリにアクセスできなかった場合
   *          Futureが失敗した場合の例外
   */
  def delete(identity: ID): Future[AsyncRepository[ID, T]]

  /**
   * 指定したエンティティを削除する。
   *
   * @param entity エンティティ
   * @return Success:
   *          非同期リポジトリ
   *         Failure:
   *          RepositoryException リポジトリにアクセスできなかった場合
   *          Futureが失敗した場合の例外
   */
  def delete(entity: T): Future[AsyncRepository[ID, T]] = delete(entity.identity)

}
