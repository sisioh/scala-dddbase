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
 * イベントタイプ。
 */
object EventType extends Enumeration {
  val Resolve, Store, Delete = Value
}

/**
 * エンティティをIOするためのトレイト。
 */
trait EntityIO

/**
 * エンティティのIOイベントを管理するためのトレイト。
 *
 * @tparam ID 識別子の型
 * @tparam T エンティティの型
 */
trait EntityIOEventSubmitter[ID <: Identity[_], T <: Entity[ID]] {
  this: EntityIO =>

  /**
   * イベントハンドラ。
   */
  type EventHandler = (T, EventType.Value) => Unit

  /**
   * イベントハンドラのリスト。
   */
  protected val eventHandlers = new ListBuffer[EventHandler]()

  /**
   * イベントハンドラを登録する。
   *
   * @param eventHandler イベントハンドラ
   * @return `Try` にラップされた `this`
   */
  def addEventHandler(eventHandler: EventHandler): Try[EntityIOEventSubmitter[ID, T]] = {
    eventHandlers += eventHandler
    Success(this)
  }

  /**
   * イベントハンドラを削除する。
   *
   * @param eventHandler イベントハンドラ
   * @return `Try` にラップされた `this`
   */
  def removeEventHandler(eventHandler: EventHandler): Try[EntityIOEventSubmitter[ID, T]] = {
    eventHandlers -= eventHandler
    Success(this)
  }

  /**
   * イベントハンドラにイベントを送信する。
   *
   * @param entity エンティティ
   * @param eventType イベントタイプ
   * @return `Try` にラップされた `this`
   */
  protected def submitToEventHandlers(entity: T, eventType: EventType.Value): Try[EntityIOEventSubmitter[ID, T]] = {
    eventHandlers.foreach(_(entity, eventType))
    Success(this)
  }

}

/**
 * [[org.sisioh.dddbase.core.model.Identity]]を用いて、[[org.sisioh.dddbase.core.model.Entity]]
 * を読み込むための責務を表すインターフェイス。
 *
 * @tparam ID 識別子の型
 * @tparam T エンティティの型
 */
trait EntityReader[ID <: Identity[_], T <: Entity[ID]] extends EntityIO {

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
   * [[org.sisioh.dddbase.core.lifecycle.EntityReader]]の`resolve`へのショートカット。
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
   * @param identity 識別子
   * @return Success:
   *         存在する場合はtrue
   *         Failure:
   *         RepositoryExceptionは、リポジトリにアクセスできなかった場合。
   */
  def contains(identity: ID): Try[Boolean]

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
 * エンティティをOptionでラップして返すための[[org.sisioh.dddbase.core.lifecycle.EntityReader]]。
 *
 * @tparam ID 識別子の型
 * @tparam T エンティティの型
 */
trait EntityReaderByOption[ID <: Identity[_], T <: Entity[ID]] {
  this: EntityReader[ID, T] =>

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
trait EntityReaderByIterable[ID <: Identity[_], T <: Entity[ID]] extends Iterable[T] {
  this: EntityReader[ID, T] =>

  def contains(identifier: ID): Try[Boolean] = Success(exists(_.identity == identifier))

}

/**
 * リポジトリの新しい状態とエンティティを保持する値オブジェクト。
 *
 * @param state リポジトリの新しい状態
 * @param entity エンティティ
 * @tparam R リポジトリの型
 * @tparam T エンティティの型
 */
case class StateWithEntity[+R, T](state: R, entity: T)

/**
 * [[org.sisioh.dddbase.core.model.Identity]]を用いて、[[org.sisioh.dddbase.core.model.Entity]]
 * を書き込むための責務を表すインターフェイス。
 *
 * @tparam ID 識別子の型
 * @tparam T エンティティの型
 */
trait EntityWriter[+R <: EntityWriter[_, ID, T], ID <: Identity[_], T <: Entity[ID]] extends EntityIO {

  /**
   * エンティティを保存する。
   *
   * @param entity 保存する対象のエンティティ
   * @return Success:
   *         リポジトリインスタンス
   *         Failure
   *         RepositoryExceptionは、リポジトリにアクセスできなかった場合。
   */
  def store(entity: T): Try[StateWithEntity[R, T]]

  /**
   * [[org.sisioh.dddbase.core.lifecycle.EntityWriter]] `store` へのショートカット。
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
  def delete(identity: ID): Try[R]

  /**
   * 指定したエンティティを削除する。
   *
   * @param entity エンティティ
   * @return Success:
   *         リポジトリインスタンス
   *         Failure:
   *         RepositoryExceptionは、リポジトリにアクセスできなかった場合。
   */
  def delete(entity: T): Try[R] = delete(entity.identity)

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
trait Repository[+R <: Repository[_, ID, T], ID <: Identity[_], T <: Entity[ID]]
  extends EntityReader[ID, T] with EntityWriter[R, ID, T]


/**
 * 述語関数に該当したエンティティを検索することができるトレイト。
 *
 * @tparam ID 識別子の型
 * @tparam T エンティティの型
 */
trait EntityReaderByPredicate[ID <: Identity[_], T <: Entity[ID]] {
  this: EntityReader[ID, T] =>

  /**
   * 述語関数に該当したエンティティを取得する。
   *
   * @param predicate 述語関数
   * @return Success:
   *         チャンク
   *         Failure:
   *         EntityNotFoundExceptionは、エンティティが見つからなかった場合
   *         RepositoryExceptionは、リポジトリにアクセスできなかった場合。
   */
  def filterByPredicate(predicate: T => Boolean, index: Option[Int] = None, maxEntities: Option[Int] = None): Try[EntitiesChunk[ID, T]]

}


/**
 * ページングによる検索を行うためのトレイト。
 *
 * @tparam ID 識別子の型
 * @tparam T エンティティの型
 */
trait EntityReaderByChunk[ID <: Identity[_], T <: Entity[ID]] {
  this: EntityReader[ID, T] =>

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