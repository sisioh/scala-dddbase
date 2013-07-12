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
package org.sisioh.dddbase.core.lifecycle

import scala.concurrent._
import org.sisioh.dddbase.core.model.{Identity, Entity}

/**
 * 非同期版[[org.sisioh.dddbase.core.lifecycle.SyncEntityReader]]。
 *
 * @see [[org.sisioh.dddbase.core.lifecycle.SyncEntityReader]]
 *
 * @tparam ID 識別子の型
 * @tparam T エンティティの型
 */
trait AsyncEntityReader[ID <: Identity[_], T <: Entity[ID]]
  extends EntityReader[ID, T, Future] {

  implicit val executor: ExecutionContext

  /**
   * 識別子に該当するエンティティを解決する。
   *
   * @see [[org.sisioh.dddbase.core.lifecycle.SyncEntityReader]] `resolve`
   *
   * @param identity 識別子
   * @return Success:
   *         非同期リポジトリ
   *         Failure:
   *         EntityNotFoundException リポジトリにアクセスできなかった場合
   *         RepositoryException リポジトリにアクセスできなかった場合
   */
  def resolve(identity: ID): Future[T]

  /**
   * 指定した識別子のエンティティが存在するかを返す。
   *
   * @param identity 識別子
   * @return Success:
   *         存在する場合はtrue
   *         Failure:
   *         EntityNotFoundException リポジトリにアクセスできなかった場合
   *         RepositoryException リポジトリにアクセスできなかった場合
   *         Futureが失敗した場合の例外
   */
  def contains(identity: ID): Future[Boolean]

}


/**
 * 非同期版[[org.sisioh.dddbase.core.lifecycle.SyncEntityWriter]]。
 *
 * @see [[org.sisioh.dddbase.core.lifecycle.SyncEntityWriter]]
 *
 * @tparam ID 識別子の型
 * @tparam T エンティティの型
 */
trait AsyncEntityWriter[+R <: AsyncEntityWriter[_, ID, T], ID <: Identity[_], T <: Entity[ID]]
  extends EntityWriter[R, ID, T, Future] {

  implicit val executor: ExecutionContext

  /**
   * エンティティを保存する。
   *
   * @see [[org.sisioh.dddbase.core.lifecycle.SyncRepository]] `store`
   *
   * @param entity 保存する対象のエンティティ
   * @return Success:
   *         非同期リポジトリ
   *         Failure:
   *         RepositoryException リポジトリにアクセスできなかった場合
   *         Futureが失敗した場合の例外
   */
  def store(entity: T): Future[RepositoryWithEntity[R, T]]

  /**
   * 識別子を指定してエンティティを削除する。
   *
   * @param identity 識別子
   * @return Success:
   *         非同期リポジトリ
   *         Failure:
   *         RepositoryException リポジトリにアクセスできなかった場合
   *         Futureが失敗した場合の例外
   */
  def delete(identity: ID): Future[R]

}

/**
 * 非同期版[[org.sisioh.dddbase.core.lifecycle.SyncRepository]]。
 *
 * @see [[org.sisioh.dddbase.core.lifecycle.SyncRepository]]
 *
 * @tparam ID 識別子の型
 * @tparam T エンティティの型
 */
trait AsyncRepository[+R <: AsyncRepository[_, ID, T], ID <: Identity[_], T <: Entity[ID]]
  extends Repository[R, ID, T, Future]
  with AsyncEntityReader[ID, T]
  with AsyncEntityWriter[R, ID, T]

/**
 * エンティティを`Option`でラップして返すための[[org.sisioh.dddbase.core.lifecycle.AsyncEntityReader]]。
 *
 * @tparam ID 識別子の型
 * @tparam T エンティティの型
 */
trait AsyncEntityReaderByOption[ID <: Identity[_], T <: Entity[ID]]
  extends EntityReaderByOption[ID, T, Future] {
  this: AsyncEntityReader[ID, T] =>

  /**
   * 識別子に該当するエンティティを解決する。
   *
   * @see [[org.sisioh.dddbase.core.lifecycle.SyncEntityReader]] `resolve`
   *
   * @param identity 識別子
   * @return Success:
   *         Option[エンティティ]
   *         Failure:
   *         Futureが失敗した場合の例外
   */
  def resolveOption(identity: ID): Future[Option[T]]

}

/**
 * 非同期版[[org.sisioh.dddbase.core.lifecycle.SyncEntityReaderByPredicate]]。
 *
 * @tparam ID 識別子の型
 * @tparam T エンティティの型
 */
trait AsyncEntityReaderByPredicate[ID <: Identity[_], T <: Entity[ID]]
  extends EntityReaderByPredicate[ID, T, Future] {
  this: AsyncEntityReader[ID, T] =>

  /**
   * 述語関数に該当したエンティティを取得する。
   *
   * @param predicate 述語関数
   * @param index チャンクのインデックス
   * @param maxEntities 1チャンク内の件数
   * @return Success:
   *         チャンク
   *         Failure:
   *         EntityNotFoundExceptionは、エンティティが見つからなかった場合
   *         RepositoryExceptionは、リポジトリにアクセスできなかった場合。
   */
  def filterByPredicate
  (predicate: T => Boolean, index: Option[Int] = None, maxEntities: Option[Int] = None) : Future[EntitiesChunk[ID, T]]

}


/**
 * 非同期版[[org.sisioh.dddbase.core.lifecycle.SyncEntityReaderByChunk]]。
 *
 * @tparam ID 識別子の型
 * @tparam T エンティティの型
 */
trait AsyncEntityReaderByChunk[ID <: Identity[_], T <: Entity[ID]]
extends EntityReaderByChunk[ID, T, Future] {
  this: AsyncEntityReader[ID, T] =>

  /**
   * エンティティをチャンク単位で検索する。
   *
   * @param index 検索するチャンクのインデックス
   * @param maxEntities 1チャンクの件数
   * @return Success:
   *         チャンク
   *         Failure:
   *         RepositoryExceptionは、リポジトリにアクセスできなかった場合。
   */
  def resolveChunk(index: Int, maxEntities: Int): Future[EntitiesChunk[ID, T]]

}

/**
 * 非同期ですべてのエンティティを取得するためのトレイト。
 *
 * @tparam ID 識別子の型
 * @tparam T エンティティの型
 */
trait AsyncEntityReaderBySeq[ID <: Identity[_], T <: Entity[ID]] {
  this: AsyncEntityReader[ID, T] =>

  /**
   * すべてのエンティティを取得する。
   *
   * @return Success:
   *         エンティティの列
   *         Failure:
   *         RepositoryExceptionは、リポジトリにアクセスできなかった場合。
   */
  def resolveAll(implicit executor: ExecutionContext): Future[Seq[T]]

}

