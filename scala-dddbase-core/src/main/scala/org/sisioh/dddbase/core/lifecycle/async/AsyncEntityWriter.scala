package org.sisioh.dddbase.core.lifecycle.async

import org.sisioh.dddbase.core.lifecycle.{EntityWriter, ResultWithEntity}
import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.concurrent.{ExecutionContext, Future}

/**
 * 非同期版[[org.sisioh.dddbase.core.lifecycle.EntityWriter]]。
 *
 * @see [[org.sisioh.dddbase.core.lifecycle.EntityWriter]]
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait AsyncEntityWriter[ID <: Identity[_], E <: Entity[ID]]
  extends EntityWriter[ID, E, Future] {

  implicit val executor: ExecutionContext

  type This <: AsyncEntityWriter[ID, E]

  /**
   * エンティティを保存する。
   *
   * @see [[org.sisioh.dddbase.core.lifecycle.Repository]] `store`
   *
   * @param entity 保存する対象のエンティティ
   * @return Success:
   *         非同期リポジトリ
   *         Failure:
   *         RepositoryException リポジトリにアクセスできなかった場合
   *         Futureが失敗した場合の例外
   */
  def store(entity: E): Future[ResultWithEntity[This, ID, E, Future]]

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
  def delete(identity: ID): Future[This]

}
