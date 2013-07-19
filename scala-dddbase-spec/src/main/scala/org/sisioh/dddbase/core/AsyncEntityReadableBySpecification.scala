package org.sisioh.dddbase.core

import org.sisioh.dddbase.core.lifecycle.EntitiesChunk
import org.sisioh.dddbase.core.lifecycle.async.AsyncEntityReader
import org.sisioh.dddbase.core.model.{Identity, Entity}
import org.sisioh.dddbase.spec.Specification
import scala.concurrent.Future

/**
 * 非同期版[[org.sisioh.dddbase.core.SyncEntityReadableBySpecification]]。
 *
 * @tparam ID 識別子の型
 * @tparam T エンティティの型
 */
trait AsyncEntityReadableBySpecification[ID <: Identity[_], T <: Entity[ID]]
  extends EntityReadableBySpecification[ID, T, Future] {
  this: AsyncEntityReader[ID, T] =>

  /**
   * [[org.sisioh.dddbase.spec.Specification]]に該当したエンティティを取得する。
   *
   * @param specification [[org.sisioh.dddbase.spec.Specification]]
   * @param index チャンクのインデックス
   * @param maxEntities 1チャンク内の件数
   * @return Success:
   *         チャンク
   *         Failure:
   *         EntityNotFoundExceptionは、エンティティが見つからなかった場合
   *         RepositoryExceptionは、リポジトリにアクセスできなかった場合。
   */
  def filterBySpecification
  (specification: Specification[T], index: Option[Int] = None, maxEntities: Option[Int] = None)
  : Future[EntitiesChunk[ID, T]]

}
