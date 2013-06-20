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
import scala.collection.mutable.ListBuffer

/**
 * 非同期にエンティティをIOするためのトレイト。
 */
trait AsyncEntityIO

/**
 * 非同期にエンティティのIOイベントを管理するためのトレイト。
 *
 * @tparam ID 識別子の型
 * @tparam T エンティティの型
 */
trait AsyncEntityIOEventSubmitter[ID <: Identity[_], T <: Entity[ID]] {
  this: AsyncEntityIO =>

  type EventHandler = (T, EventType.Value) => Unit

  protected val eventHandlers = new ListBuffer[Future[EventHandler]]()

  /**
   * イベントハンドラを登録する。
   *
   * @param eventHandler イベントハンドラ
   * @param executor [[scala.concurrent.ExecutionContext]]
   * @return `Future` にラップされた `this`
   */
  def addEventHandler(eventHandler: EventHandler)(implicit executor: ExecutionContext): Future[AsyncEntityIOEventSubmitter[ID, T]] = future {
    eventHandlers += future(eventHandler)
    this
  }

  /**
   * イベントハンドラを削除する。
   *
   * @param eventHandler イベントハンドラ
   * @param executor [[scala.concurrent.ExecutionContext]]
   * @return `Future` にラップされた `this`
   */
  def removeEventHandler(eventHandler: EventHandler)(implicit executor: ExecutionContext): Future[AsyncEntityIOEventSubmitter[ID, T]] = future {
    eventHandlers -= future(eventHandler)
    this
  }

  /**
   * イベントハンドラにイベントを送信する。
   *
   * @param entity エンティティ
   * @param eventType イベントタイプ
   * @param executor [[scala.concurrent.ExecutionContext]]
   * @return
   */
  protected def submitToEventHandlers(entity: T, eventType: EventType.Value)(implicit executor: ExecutionContext): Future[AsyncEntityIOEventSubmitter[ID, T]] =
    future {
      eventHandlers.map {
        eventHandler =>
          eventHandler.map {
            e2 =>
              e2(entity, eventType)
          }
      }
      this
    }

}

/**
 * 非同期版[[org.sisioh.dddbase.core.EntityReader]]。
 *
 * @see [[org.sisioh.dddbase.core.EntityReader]]
 *
 * @tparam ID 識別子の型
 * @tparam T エンティティの型
 */
trait AsyncEntityReader[ID <: Identity[_], T <: Entity[ID]] extends AsyncEntityIO {

  /**
   * 識別子に該当するエンティティを解決する。
   *
   * @see [[org.sisioh.dddbase.core.EntityReader]] `resolve`
   *
   * @param identity 識別子
   * @return Success:
   *         非同期リポジトリ
   *         Failure:
   *         EntityNotFoundException リポジトリにアクセスできなかった場合
   *         RepositoryException リポジトリにアクセスできなかった場合
   */
  def resolve(identity: ID)(implicit executor: ExecutionContext): Future[T]

  /**
   * [[org.sisioh.dddbase.core.AsyncEntityReader]] `resolve`へのショートカット。
   *
   * @param identity 識別子
   * @return Success:
   *         非同期リポジトリ
   *         Failure:
   *         EntityNotFoundException リポジトリにアクセスできなかった場合
   *         RepositoryException リポジトリにアクセスできなかった場合
   */
  def apply(identity: ID)(implicit executor: ExecutionContext) = resolve(identity)

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
  def contains(identity: ID)(implicit executor: ExecutionContext): Future[Boolean]

  /**
   * 指定したエンティティが存在するかを返す。
   *
   * @param entity エンティティ
   * @return Success:
   *         存在する場合はtrue
   *         Failure:
   *         EntityNotFoundException リポジトリにアクセスできなかった場合
   *         RepositoryException リポジトリにアクセスできなかった場合
   *         Futureが失敗した場合の例外
   */
  def contains(entity: T)(implicit executor: ExecutionContext): Future[Boolean] = contains(entity.identity)

}


/**
 * 非同期版[[org.sisioh.dddbase.core.EntityWriter]]。
 *
 * @see [[org.sisioh.dddbase.core.EntityWriter]]
 *
 * @tparam ID 識別子の型
 * @tparam T エンティティの型
 */
trait AsyncEntityWriter[ID <: Identity[_], T <: Entity[ID]] extends AsyncEntityIO {

  /**
   * エンティティを保存する。
   *
   * @see [[org.sisioh.dddbase.core.Repository]] `store`
   *
   * @param entity 保存する対象のエンティティ
   * @return Success:
   *         非同期リポジトリ
   *         Failure:
   *         RepositoryException リポジトリにアクセスできなかった場合
   *         Futureが失敗した場合の例外
   */
  def store(entity: T)(implicit executor: ExecutionContext): Future[AsyncRepository[ID, T]]

  /**
   * [[org.sisioh.dddbase.core.AsyncRepository]] `store`へのショートカット。
   *
   * @param identifier 識別子
   * @param entity 保存する対象のエンティティ
   * @return Success:
   *         非同期リポジトリ
   *         Failure:
   *         RepositoryException リポジトリにアクセスできなかった場合
   *         Futureが失敗した場合の例外
   */
  def update(identifier: ID, entity: T)(implicit executor: ExecutionContext) = store(entity)

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
  def delete(identity: ID)(implicit executor: ExecutionContext): Future[AsyncRepository[ID, T]]

  /**
   * 指定したエンティティを削除する。
   *
   * @param entity エンティティ
   * @return Success:
   *         非同期リポジトリ
   *         Failure:
   *         RepositoryException リポジトリにアクセスできなかった場合
   *         Futureが失敗した場合の例外
   */
  def delete(entity: T)(implicit executor: ExecutionContext): Future[AsyncRepository[ID, T]] = delete(entity.identity)

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
 * エンティティを`Option`でラップして返すための[[org.sisioh.dddbase.core.AsyncEntityReader]]。
 *
 * @tparam ID 識別子の型
 * @tparam T エンティティの型
 */
trait AsyncEntityReaderByOption[ID <: Identity[_], T <: Entity[ID]] {
  this: AsyncEntityReader[ID, T] =>

  /**
   * 識別子に該当するエンティティを解決する。
   *
   * @see [[org.sisioh.dddbase.core.EntityReader]] `resolve`
   *
   * @param identity 識別子
   * @return Success:
   *         Option[エンティティ]
   *         Failure:
   *         Futureが失敗した場合の例外
   */
  def resolveOption(identity: ID)(implicit executor: ExecutionContext): Future[Option[T]]

}

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
   * @param index チャンクのインデックス
   * @param maxEntities 1チャンク内の件数
   * @return Success:
   *         チャンク
   *         Failure:
   *         EntityNotFoundExceptionは、エンティティが見つからなかった場合
   *         RepositoryExceptionは、リポジトリにアクセスできなかった場合。
   */
  def filterByPredicate(predicate: T => Boolean, index: Option[Int] = None, maxEntities: Option[Int] = None)(implicit executor: ExecutionContext): Future[EntitiesChunk[ID, T]]

}



/**
 * 非同期版[[org.sisioh.dddbase.core.EntityReaderByChunk]]。
 *
 * @tparam ID 識別子の型
 * @tparam T エンティティの型
 */
trait AsyncEntityReaderByChunk[ID <: Identity[_], T <: Entity[ID]] {
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
  def resolvePage(index: Int, maxEntities: Int)(implicit executor: ExecutionContext): Future[EntitiesChunk[ID, T]]
}
