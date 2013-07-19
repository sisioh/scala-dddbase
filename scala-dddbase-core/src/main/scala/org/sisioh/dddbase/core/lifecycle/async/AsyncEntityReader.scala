package org.sisioh.dddbase.core.lifecycle.async

import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.concurrent.{ExecutionContext, Future}
import org.sisioh.dddbase.core.lifecycle.EntityReader

/**
 * 非同期版[[org.sisioh.dddbase.core.lifecycle.EntityReader]]。
 *
 * @see [[org.sisioh.dddbase.core.lifecycle.EntityReader]]
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait AsyncEntityReader[ID <: Identity[_], E <: Entity[ID]]
  extends EntityReader[ID, E, Future] {

  implicit val executor: ExecutionContext

  /**
   * 識別子に該当するエンティティを解決する。
   *
   * @see [[org.sisioh.dddbase.core.lifecycle.EntityReader]] `resolve`
   *
   * @param identity 識別子
   * @return Success:
   *         非同期リポジトリ
   *         Failure:
   *         EntityNotFoundException リポジトリにアクセスできなかった場合
   *         RepositoryException リポジトリにアクセスできなかった場合
   */
  def resolve(identity: ID): Future[E]

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
