package org.sisioh.dddbase.core.lifecycle.async

import org.sisioh.dddbase.core.lifecycle.{EntityIOContext, EntityReadableAsChunk, EntitiesChunk}
import org.sisioh.dddbase.core.model.{Entity, Identifier}
import scala.concurrent.Future

/**
 * 非同期版[[org.sisioh.dddbase.core.lifecycle.EntityReadableAsChunk]]。
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait AsyncEntityReadableAsChunk[ID <: Identifier[_], E <: Entity[ID]]
  extends EntityReadableAsChunk[ID, E, Future] {
  this: AsyncEntityReader[ID, E] =>

  /**
   * @return Success:
   *         チャンク
   *         Failure:
   *         RepositoryExceptionは、リポジトリにアクセスできなかった場合。
   */
  def resolveAsChunk(index: Int, maxEntities: Int)
                  (implicit ctx: EntityIOContext[Future]): Future[EntitiesChunk[ID, E]]

}
