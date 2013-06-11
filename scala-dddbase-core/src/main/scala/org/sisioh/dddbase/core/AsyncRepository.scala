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
import org.sisioh.dddbase.spec.Specification

/**
 * 非同期版[[org.sisioh.dddbase.core.EntityReader]]。
 *
 * @see [[org.sisioh.dddbase.core.EntityReader]]
 *
 * @tparam ID 識別子の型
 * @tparam T エンティティの型
 */
trait AsyncEntityReader[ID <: Identity[_], T <: Entity[ID]] {

  /**
   * 識別子に該当するエンティティを解決する。
   *
   * @see [[org.sisioh.dddbase.core.EntityReader!.resolve]]
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
   * @see [[org.sisioh.dddbase.core.EntityReader!.resolve]]
   *
   * @param identity 識別子
   * @return Success:
   *          Option[エンティティ]
   *         Failure:
   *          Futureが失敗した場合の例外
   */
  def resolveOption(identity: ID): Future[Option[T]]

  /**
   * [[org.sisioh.dddbase.core.AsyncEntityReader!.resolve]]へのショートカット。
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
 * 非同期版[[org.sisioh.dddbase.core.EntityWriter]]。
 *
 * @see [[org.sisioh.dddbase.core.EntityWriter]]
 *
 * @tparam ID 識別子の型
 * @tparam T エンティティの型
 */
trait AsyncEntityWriter[ID <: Identity[_], T <: Entity[ID]] {

  /**
   * エンティティを保存する。
   *
   * @see [[org.sisioh.dddbase.core.Repository!.store]]
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
   * [[org.sisioh.dddbase.core.AsyncRepository!.store]]へのショートカット。
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
   * 識別子を指定してエンティティを削除する。
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

/**
 * 非同期版[[org.sisioh.dddbase.core.Repository]]。
 *
 * @see [[org.sisioh.dddbase.core.Repository]]
 *
 * @tparam ID 識別子の型
 * @tparam T エンティティの型
 */
trait AsyncRepository[ID <: Identity[_], T <: Entity[ID]] extends AsyncEntityReader[ID, T] with AsyncEntityWriter[ID, T]

/**
 * 非同期版[[org.sisioh.dddbase.core.EntityReaderByPredicate]]。
 *
 * @tparam ID 識別子の型
 * @tparam T エンティティの型
 */
trait AsyncEntityReaderByPredicate[ID <: Identity[_], T <: Entity[ID]] {
  this: AsyncEntityReader[ID, T] =>

  /**
   * 述語関数に該当したエンティティを取得する。
   *
   * @param predicate 述語関数
   * @return Success:
   *         エンティティのリスト
   *         Failure:
   *         EntityNotFoundExceptionは、エンティティが見つからなかった場合
   *         RepositoryExceptionは、リポジトリにアクセスできなかった場合。
   */
  def filterByPredicate(predicate: T => Boolean): Future[List[T]]

}

/**
 * 非同期版[[org.sisioh.dddbase.core.EntityReaderBySpecification]]。
 *
 * @tparam ID 識別子の型
 * @tparam T エンティティの型
 */
trait AsyncEntityReaderBySpecification[ID <: Identity[_], T <: Entity[ID]] {
  this: AsyncEntityReader[ID, T] =>

  /**
   * [[org.sisioh.dddbase.spec.Specification]]に該当したエンティティを取得する。
   *
   * @param specification [[org.sisioh.dddbase.spec.Specification]]
   * @return Success:
   *         エンティティのリスト
   *         Failure:
   *         EntityNotFoundExceptionは、エンティティが見つからなかった場合
   *         RepositoryExceptionは、リポジトリにアクセスできなかった場合。
   */
  def filterBySpecification(specification: Specification[T]): Future[List[T]]

}

/**
 * 非同期版[[org.sisioh.dddbase.core.EntityReaderByCallback]]。
 *
 * @tparam ID 識別子の型
 * @tparam T エンティティの型
 */
trait AsyncEntityReaderByCallback[ID <: Identity[_], T <: Entity[ID]] {
  this: AsyncEntityReader[ID, T] =>

  /**
   * 識別子に該当するエンティティを解決する。
   *
   * callbackの引数である`Try[T]`は[[org.sisioh.dddbase.core.EntityReader!.resolve]]の戻り値と同じ結果を返す
   *
   * @see [[org.sisioh.dddbase.core.EntityReader!.resolve]]
   *
   * @param callback コールバック
   * @tparam R コールバックの戻り値の型
   * @return コールバックの戻り値
   */
  def resolveByCallback[R](callback: Future[T] => Future[R]): Future[R]

}

/**
 * 非同期版[[org.sisioh.dddbase.core.EntityReaderByPaging]]。
 *
 * @tparam ID 識別子の型
 * @tparam T エンティティの型
 */
trait AsyncEntityReaderByPaging[ID <: Identity[_], T <: Entity[ID]] {
  this: AsyncEntityReader[ID, T] =>

  /**
   * ページを表すクラス。
   *
   * @author j5ik2o
   */
  case class Page(size: Int, entities: Seq[T])

  /**
   * エンティティをページ単位で検索する。
   *
   * @param pageSize 1ページの件数
   * @param index 検索するページのインデックス
   * @return Success:
   *         ページ
   *         Failure:
   *         RepositoryExceptionは、リポジトリにアクセスできなかった場合。
   */
  def resolvePage(pageSize: Int, index: Int): Future[Page]
}
