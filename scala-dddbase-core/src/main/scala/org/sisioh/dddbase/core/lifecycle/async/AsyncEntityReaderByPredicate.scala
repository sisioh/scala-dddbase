package org.sisioh.dddbase.core.lifecycle.async

import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.concurrent.Future
import org.sisioh.dddbase.core.lifecycle.{EntityReaderByPredicate, EntitiesChunk}

/**
 * 非同期版[[org.sisioh.dddbase.core.lifecycle.EntityReaderByPredicate]]。
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait AsyncEntityReaderByPredicate[ID <: Identity[_], E <: Entity[ID]]
  extends EntityReaderByPredicate[ID, E, Future] {
  this: AsyncEntityReader[ID, E] =>

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
  def filterByPredicate
  (predicate: E => Boolean, index: Option[Int] = None, maxEntities: Option[Int] = None): Future[EntitiesChunk[ID, E]]

}
