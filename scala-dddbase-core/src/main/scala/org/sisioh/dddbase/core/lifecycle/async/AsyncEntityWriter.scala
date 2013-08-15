package org.sisioh.dddbase.core.lifecycle.async

import org.sisioh.dddbase.core.lifecycle.{EntityIOContext, EntityWriter}
import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.concurrent.Future

/**
 * 非同期版[[org.sisioh.dddbase.core.lifecycle.EntityWriter]]。
 *
 * @see [[org.sisioh.dddbase.core.lifecycle.EntityWriter]]
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait AsyncEntityWriter[ID <: Identity[_], E <: Entity[ID]]
  extends AsyncEntityIO with EntityWriter[ID, E, Future] {

  type This <: AsyncEntityWriter[ID, E]

  /**
   * エンティティを保存する。
   *
   * @see [[org.sisioh.dddbase.core.lifecycle.Repository]] `store`
   *
   * @param entity 保存する対象のエンティティ
   * @return Success:
   *         リポジトリインスタンスと保存されたエンティティ
   *         Failure:
   *         RepositoryException リポジトリにアクセスできなかった場合
   *         Futureが失敗した場合の例外
   */
  def store(entity: E)(implicit ctx: EntityIOContext[Future]): Future[AsyncResultWithEntity[This, ID, E]]

  /**
   * 識別子を指定してエンティティを削除する。
   *
   * @param identity 識別子
   * @return Success:
   *         リポジトリインスタンスと削除されたエンティティ
   *         Failure:
   *         RepositoryException リポジトリにアクセスできなかった場合
   *         Futureが失敗した場合の例外
   */
  def delete(identity: ID)(implicit ctx: EntityIOContext[Future]): Future[AsyncResultWithEntity[This, ID, E]]

}
