package org.sisioh.dddbase.core

import org.sisioh.dddbase.spec.Specification
import scala.concurrent.{Future, ExecutionContext}

/**
 * 非同期版[[org.sisioh.dddbase.core.EntityReaderBySpecification]]。
 *
 * @tparam ID 識別子の型
 * @tparam T エンティティの型
 */
trait AsyncEntityReaderBySpecification[ID <: Identity[_], T <: Entity[ID]] {
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
  def filterBySpecification(specification: Specification[T], index: Option[Int] = None, maxEntities: Option[Int] = None)(implicit executor: ExecutionContext): Future[EntitiesChunk[ID, T]]

}
