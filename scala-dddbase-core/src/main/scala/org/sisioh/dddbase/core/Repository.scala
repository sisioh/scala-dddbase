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

/**
 * [[java.lang.Identifier]]を用いて、[[org.sisioh.dddbase.core.Entity]]
 * を検索する責務を表すインターフェイス。
 *
 * @author j5ik2o
 */
trait EntityResolver[T <: Entity] extends Iterable[T] {

  /**
   * 識別子に該当するエンティティを取得する。
   *
   *  @param identifier 識別子
   *  @return エンティティ
   *
   *  @throws IllegalArgumentException
   *  @throws EntityNotFoundException エンティティが見つからなかった場合
   *  @throws RepositoryException リポジトリにアクセスできない場合
   */
  def resolve(identifier: Identifier): T

  def apply(identifier: Identifier) = resolve(identifier)

  /**
   * 指定した識別子のエンティティが存在するかを返す。
   *
   *  @param identifier 識別子
   *  @return 存在する場合はtrue
   *  @throws RepositoryException リポジトリにアクセスできない場合
   */
  def contains(identifier: Identifier): Boolean = exists(_.identifier == identifier)

  /**
   * 指定したのエンティティが存在するかを返す。
   *
   *  @param entity エンティティ
   *  @return 存在する場合はtrue
   *  @throws RepositoryException リポジトリにアクセスできない場合
   */
  def contains(entity: T): Boolean = exists(_ == entity)

}

/**
 * 基本的なリポジトリのトレイト。
 *  リポジトリとして、基本的に必要な機能を定義するトレイト。
 *
 *  @tparam T エンティティの型
 *  @tparam ID エンティティの識別子の型
 *
 *  @author j5ik2o
 */
trait Repository[T <: Entity] extends EntityResolver[T] {

  /**
   * エンティティを保存する。
   *
   *  @param entity 保存する対象のエンティティ
   *  @throws RepositoryException リポジトリにアクセスできない場合
   */
  def store(entity: T)

  def update(identifier: Identifier, entity: T) = store(entity)

  /**
   * 指定した識別子のエンティティを削除する。
   *
   *  @param identity 識別子
   *  @throws EntityNotFoundException 指定された識別子を持つエンティティが見つからなかった場合
   *  @throws RepositoryException リポジトリにアクセスできない場合
   */
  def delete(identity: Identifier)

  /**
   * 指定したエンティティを削除する。
   *
   *  @param entity エンティティ
   *  @throws EntityNotFoundException 指定された識別子を持つエンティティが見つからなかった場合
   *  @throws RepositoryException リポジトリにアクセスできない場合
   */
  def delete(entity: T)

}

/**
 * ページングによる検索を行うためのトレイト。
 *
 *  @author j5ik2o
 */
trait PagingFindForRepository[T <: Entity] {
  this: Repository[T] =>

  /**
   * ページを表すクラス。
   *
   *  @author j5ik2o
   */
  case class Page(size: Int, entities: List[T])

  /**
   * エンティティをページ単位で検索する。
   *
   *  @param pageSize 1ページの件数
   *  @param index 検索するページのインデックス
   *  @return ページ
   */
  def findPage(pageSize: Int, index: Int): Page
}