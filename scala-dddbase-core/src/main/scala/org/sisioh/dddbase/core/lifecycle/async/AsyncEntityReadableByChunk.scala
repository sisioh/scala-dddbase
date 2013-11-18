package org.sisioh.dddbase.core.lifecycle.async

import org.sisioh.dddbase.core.lifecycle.{EntityIOContext, EntityReadableByChunk, EntitiesChunk}
import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.concurrent.Future

/**
 * 非同期版[[org.sisioh.dddbase.core.lifecycle.EntityReadableByChunk]]。
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait AsyncEntityReadableByChunk[CTX <: EntityIOContext[Future], ID <: Identity[_], E <: Entity[ID]]
  extends EntityReadableByChunk[CTX, ID, E, Future] {
  this: AsyncEntityReader[CTX, ID, E] =>

  /**
   * @return Success:
   *         チャンク
   *         Failure:
   *         RepositoryExceptionは、リポジトリにアクセスできなかった場合。
   */
  def resolveChunk(index: Int, maxEntities: Int)
                  (implicit ctx: CTX): Future[EntitiesChunk[ID, E]]

}
