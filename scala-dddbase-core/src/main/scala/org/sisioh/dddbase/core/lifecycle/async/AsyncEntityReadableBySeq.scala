package org.sisioh.dddbase.core.lifecycle.async

import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.concurrent.Future
import org.sisioh.dddbase.core.lifecycle.EntityIOContext

/**
 * 非同期ですべてのエンティティを取得するためのトレイト。
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait AsyncEntityReadableBySeq[ID <: Identity[_], E <: Entity[ID]] {
  this: AsyncEntityReader[ID, E] =>

  /**
   * @return Success:
   *         エンティティの列
   *         Failure:
   *         RepositoryExceptionは、リポジトリにアクセスできなかった場合。
   */
  def resolveAll(implicit ctx: EntityIOContext[Future]): Future[Seq[E]]

}
