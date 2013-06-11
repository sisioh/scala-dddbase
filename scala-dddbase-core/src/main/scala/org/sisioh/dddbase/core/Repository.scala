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

import scala.util._

/**
 * [[org.sisioh.dddbase.core.Identity]]を用いて、[[org.sisioh.dddbase.core.Entity]]
 * を読み込むための責務を表すインターフェイス。
 *
 * @author j5ik2o
 */
trait EntityReader[ID <: Identity[_], T <: Entity[ID]] {

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

  /**
   * [[org.sisioh.dddbase.core.EntityReader!.r e s o l v e]]へのショートカット。
   *
   * @param identity 識別子
   * @return Success:
   *         エンティティ
   *         Failure:
   *         EntityNotFoundExceptionは、エンティティが見つからなかった場合
   *         RepositoryExceptionは、リポジトリにアクセスできなかった場合。
   */
  def apply(identity: ID) = resolve(identity)

  /**
   * 指定した識別子のエンティティが存在するかを返す。
   *
   * @param identifier 識別子
   * @return Success:
   *         存在する場合はtrue
   *         Failure:
   *         RepositoryExceptionは、リポジトリにアクセスできなかった場合。
   */
  def contains(identifier: ID): Try[Boolean]

  /**
   * 指定したのエンティティが存在するかを返す。
   *
   * @param entity エンティティ
   * @return Success:
   *         存在する場合はtrue
   *         Failure:
   *         RepositoryExceptionは、リポジトリにアクセスできなかった場合。
   */
  def contains(entity: T): Try[Boolean] = contains(entity.identity)

}

/**
 * `scala.collection.Iterable`を実装するためのトレイト。
 */
trait EntityIterableReader[ID <: Identity[_], T <: Entity[ID]] extends Iterable[T] {
  this: EntityReader[ID, T] =>

  def contains(identifier: ID): Try[Boolean] = Success(exists(_.identity == identifier))

}

/**
 * [[org.sisioh.dddbase.core.Identity]]を用いて、[[org.sisioh.dddbase.core.Entity]]
 * を書き込むための責務を表すインターフェイス。
 *
 * @author j5ik2o
 */
trait EntityWriter[ID <: Identity[_], T <: Entity[ID]] {

  /**
   * エンティティを保存する。
   *
   * @param entity 保存する対象のエンティティ
   * @return Success:
   *         リポジトリインスタンス
   *         Failure:
   *         RepositoryExceptionは、リポジトリにアクセスできなかった場合。
   */
  def store(entity: T): Try[Repository[ID, T]]

  /**
   * [[org.sisioh.dddbase.core.Repository!.store]] へのショートカット。
   *
   * @param identity 識別子
   * @param entity 保存する対象のエンティティ
   * @return Success:
   *         リポジトリインスタンス
   *         Failure:
   *         RepositoryExceptionは、リポジトリにアクセスできなかった場合。
   */
  def update(identity: ID, entity: T) = store(entity)

  /**
   * 指定した識別子のエンティティを削除する。
   *
   * @param identity 識別子
   * @return Success:
   *         リポジトリインスタンス
   *         Failure:
   *         RepositoryExceptionは、リポジトリにアクセスできなかった場合。
   */
  def delete(identity: ID): Try[Repository[ID, T]]

  /**
   * 指定したエンティティを削除する。
   *
   * @param entity エンティティ
   * @return Success:
   *         リポジトリインスタンス
   *         Failure:
   *         RepositoryExceptionは、リポジトリにアクセスできなかった場合。
   */
  def delete(entity: T): Try[Repository[ID, T]] = delete(entity.identity)

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
 *
 * @author j5ik2o
 */
trait Repository[ID <: Identity[_], T <: Entity[ID]] extends EntityReader[ID, T] with EntityWriter[ID, T]


/**
 * 解決したエンティティをコールバックで返すためのトレイト。
 *
 * @tparam ID 識別子の型
 * @tparam T エンティティの型
 */
trait CallbackEntityReader[ID <: Identity[_], T <: Entity[ID]] {
  this: EntityReader[ID, T] =>

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
  def resolve[R](callback: Try[T] => R): R

}

/**
 * ページングによる検索を行うためのトレイト。
 *
 * @author j5ik2o
 */
trait PagingEntityReader[ID <: Identity[_], T <: Entity[ID]] {
  this: EntityReader[ID, T] =>

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
  def resolvePage(pageSize: Int, index: Int): Try[Page]
}