package org.sisioh.dddbase.core.lifecycle.async

import org.sisioh.dddbase.core.lifecycle.{EntityIOContext, EntityReadableAsPredicate, EntitiesChunk}
import org.sisioh.dddbase.core.model.{Entity, Identifier}
import scala.concurrent.Future

/**
 * 非同期版[[org.sisioh.dddbase.core.lifecycle.EntityReadableAsPredicate]]。
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait AsyncEntityReadableAsPredicate[ID <: Identifier[_], E <: Entity[ID]]
  extends EntityReadableAsPredicate[ID, E, Future] {
  this: AsyncEntityReader[ID, E] =>

  /**
   * @return Success:
   *         チャンク
   *         Failure:
   *         EntityNotFoundExceptionは、エンティティが見つからなかった場合
   *         RepositoryExceptionは、リポジトリにアクセスできなかった場合。
   */
  def filterBy
  (predicate: E => Boolean, index: Option[Int] = None, maxEntities: Option[Int] = None)
  (implicit ctx: EntityIOContext[Future]): Future[EntitiesChunk[ID, E]]

}
