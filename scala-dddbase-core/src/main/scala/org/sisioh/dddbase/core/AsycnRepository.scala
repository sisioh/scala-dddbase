package org.sisioh.dddbase.core

import scala.concurrent._
import scala.util.Try

trait AsyncEntityResolver[ID, T <: Entity[ID]] {

  def resolve(identifier: Identity[ID]): Future[T]

  def resolveOption(identifier: Identity[ID]): Future[Option[T]]

  def apply(identifier: Identity[ID]) = resolve(identifier)

  def contains(identifier: Identity[ID]): Future[Boolean]

  def contains(entity: T): Future[Boolean] = contains(entity.identity)

}


trait AsyncRepository[ID, T <: Entity[ID]] extends AsyncEntityResolver[ID, T] {

  /**
   * storeメソッドの非同期版
   *
   * @see [[org.sisioh.dddbase.core.Repository.store()]]
   *
   * @param entity 保存する対象のエンティティ
   */
  def store(entity: T): Future[AsyncRepository[ID, T]]

  def update(identifier: Identity[ID], entity: T) = store(entity)

  /**
   * deleteメソッドの非同期版
   *
   * @see [[org.sisioh.dddbase.core.Repository.delete()]]
   *
   * @param identity 識別子
   */
  def delete(identity: Identity[ID]): Future[AsyncRepository[ID, T]]

  /**
   * 指定したエンティティを削除する。
   *
   * @param entity エンティティ
   */
  def delete(entity: T): Future[AsyncRepository[ID, T]] = delete(entity.identity)

}
