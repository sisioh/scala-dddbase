package org.sisioh.dddbase.core

import scala.concurrent._
import scala.util.Try

/**
 * 非同期版エンティティリゾルバ。
 *
 * @tparam ID 識別子の型
 * @tparam T エンティティの型
 */
trait AsyncEntityResolver[ID <: Identity[_], T <: Entity[ID]] {

  /**
   * 識別子に該当するエンティティを取得する。
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
   * 識別子に該当するエンティティを取得する。
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
 * 非同期版リポジトリ。
 *
 * @tparam ID 識別子の型
 * @tparam T エンティティの型
 */
trait AsyncRepository[ID <: Identity[_], T <: Entity[ID]] extends AsyncEntityResolver[ID, T] {

  /**
   * storeメソッドの非同期版
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

  def update(identifier: ID, entity: T) = store(entity)

  /**
   * deleteメソッドの非同期版
   *
   * @see [[org.sisioh.dddbase.core.Repository.delete( )]]
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
