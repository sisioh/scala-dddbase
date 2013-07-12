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

import org.sisioh.dddbase.core.model.{Identity, Entity}
import scala.collection.mutable.ListBuffer
import scala.util._


/**
 * [[org.sisioh.dddbase.core.model.Identity]]を用いて、[[org.sisioh.dddbase.core.model.Entity]]
 * を読み込むための責務を表すインターフェイス。
 *
 * @tparam ID 識別子の型
 * @tparam T エンティティの型
 */
trait SyncEntityReader[ID <: Identity[_], T <: Entity[ID]] extends EntityReader[ID, T, Try] {

  /**
   * 識別子に該当するエンティティを解決する。
   *
   * @param identity 識別子
   * @return Success:
   *         エンティティ
   *         Failure:
   *         EntityNotFoundExceptionは、エンティティが見つからなかった場合
   *         RepositoryExceptionは、リポジトリにアクセスできなかった場合。
   */
  def resolve(identity: ID): Try[T]

  /**
   * 指定した識別子のエンティティが存在するかを返す。
   *
   * @param identity 識別子
   * @return Success:
   *         存在する場合はtrue
   *         Failure:
   *         RepositoryExceptionは、リポジトリにアクセスできなかった場合。
   */
  def contains(identity: ID): Try[Boolean]

}

/**
 * エンティティをOptionでラップして返すための[[org.sisioh.dddbase.core.lifecycle.SyncEntityReader]]。
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

/**
 * `scala.collection.Iterable`を実装するためのトレイト。
 *
 * @tparam ID 識別子の型
 * @tparam T エンティティの型
 */
trait SyncEntityReaderByIterable[ID <: Identity[_], T <: Entity[ID]] extends Iterable[T] {
  this: SyncEntityReader[ID, T] =>

  def contains(identifier: ID): Try[Boolean] = Success(exists(_.identity == identifier))

}


/**
 * [[org.sisioh.dddbase.core.model.Identity]]を用いて、[[org.sisioh.dddbase.core.model.Entity]]
 * を書き込むための責務を表すインターフェイス。
 *
 * @tparam ID 識別子の型
 * @tparam T エンティティの型
 */
trait SyncEntityWriter[+R <: SyncEntityWriter[_, ID, T], ID <: Identity[_], T <: Entity[ID]]
  extends EntityWriter[R, ID, T, Try] {

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

/**
 * 基本的なリポジトリのトレイト。
 * リポジトリとして、基本的に必要な機能を定義するトレイト。
 *
 * リポジトリの状態を変更するメソッドの戻り値としては、
 * Immutableなリポジトリは新しいリポジトリインスタンスを返し、
 * Mutableなリポジトリは同一インスタンスを返すこと、を推奨する。
 *
 * @tparam T エンティティの型
 * @tparam ID エンティティの識別子の型
 */
trait SyncRepository[+R <: SyncRepository[_, ID, T], ID <: Identity[_], T <: Entity[ID]]
  extends Repository[R, ID, T, Try]
  with SyncEntityReader[ID, T]
  with SyncEntityWriter[R, ID, T]

/**
 * 述語関数に該当したエンティティを検索することができるトレイト。
 *
 * @tparam ID 識別子の型
 * @tparam T エンティティの型
 */
trait SyncEntityReaderByPredicate[ID <: Identity[_], T <: Entity[ID]]
  extends EntityReaderByPredicate[ID, T, Try] {
  this: SyncEntityReader[ID, T] =>

}


/**
 * ページングによる検索を行うためのトレイト。
 *
 * @tparam ID 識別子の型
 * @tparam T エンティティの型
 */
trait SyncEntityReaderByChunk[ID <: Identity[_], T <: Entity[ID]]
extends EntityReaderByChunk[ID, T, Try]{
  this: SyncEntityReader[ID, T] =>

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
  def resolveChunk(index: Int, maxEntities: Int): Try[EntitiesChunk[ID, T]]
}