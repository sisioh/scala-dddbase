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

import scalaz.Identity

/**
 * [[scalaz.Identity]]を用いて、[[org.sisioh.dddbase.core.Entity]]
 * を検索する責務を表すインターフェイス。
 *
 * @author j5ik2o
 */
trait EntityResolver[T <: Entity[ID], ID <: java.io.Serializable] {

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
  def resolve(identifier: Identity[ID]): T
  
  def resolveOption(identifier: Identity[ID]): Option[T]

  def apply(identifier: Identity[ID]) = resolve(identifier)

  /**
   * 指定した識別子のエンティティが存在するかを返す。
   *
   *  @param identifier 識別子
   *  @return 存在する場合はtrue
   *  @throws RepositoryException リポジトリにアクセスできない場合
   */
  def contains(identifier: Identity[ID]): Boolean

  /**
   * 指定したのエンティティが存在するかを返す。
   *
   *  @param entity エンティティ
   *  @return 存在する場合はtrue
   *  @throws RepositoryException リポジトリにアクセスできない場合
   */
  def contains(entity: T): Boolean

}

trait EntityIterableResolver[T <: Entity[ID], ID <: java.io.Serializable] extends Iterable[T] {
  this: EntityResolver[T,ID] =>

  def contains(identifier: Identity[ID]): Boolean = exists(_.identity == identifier)

  def contains(entity: T): Boolean = exists(_ == entity)

}

/**
 * 基本的なリポジトリのトレイト。
 *  リポジトリとして、基本的に必要な機能を定義するトレイト。
 *
 * @tparam T エンティティの型
 * @tparam ID エンティティの識別子の型
 *
 * @author j5ik2o
 */
trait Repository[T <: Entity[ID], ID <: java.io.Serializable] extends EntityResolver[T,ID] {

  /**
   * エンティティを保存する。
   *
   * @param entity 保存する対象のエンティティ
   * @throws RepositoryException リポジトリにアクセスできない場合
   */
  def store(entity: T): Unit

  def update(identifier: Identity[ID], entity: T) = store(entity)

  /**
   * 指定した識別子のエンティティを削除する。
   *
   * @param identity 識別子
   * @throws EntityNotFoundException 指定された識別子を持つエンティティが見つからなかった場合
   * @throws RepositoryException リポジトリにアクセスできない場合
   */
  def delete(identity: Identity[ID]): Unit

  /**
   * 指定したエンティティを削除する。
   *
   * @param entity エンティティ
   * @throws EntityNotFoundException 指定された識別子を持つエンティティが見つからなかった場合
   * @throws RepositoryException リポジトリにアクセスできない場合
   */
  def delete(entity: T): Unit

}

trait CallbackEntityResolver[T <: Entity[ID], ID <: java.io.Serializable] {
  this: EntityResolver[T,ID] =>

  def resolve[R](callbak: T => R): R
}

/**
 * ページングによる検索を行うためのトレイト。
 *
 * @author j5ik2o
 */
trait PagingEntityResolver[T <: Entity[ID], ID <: java.io.Serializable] {
  this: EntityResolver[T,ID] =>

  /**
   * ページを表すクラス。
   *
   * @author j5ik2o
   */
  case class Page(size: Int, entities: List[T])

  /**
   * エンティティをページ単位で検索する。
   *
   * @param pageSize 1ページの件数
   * @param index 検索するページのインデックス
   * @return ページ
   */
  def resolvePage(pageSize: Int, index: Int): Page
}