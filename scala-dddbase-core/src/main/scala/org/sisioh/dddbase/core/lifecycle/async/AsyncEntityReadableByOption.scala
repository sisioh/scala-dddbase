package org.sisioh.dddbase.core.lifecycle.async

import org.sisioh.dddbase.core.lifecycle.{EntityIOContext, EntityReadableByOption}
import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.concurrent.Future

/**
 * エンティティを`Option`でラップして返すための[[org.sisioh.dddbase.core.lifecycle.async.AsyncEntityReader]]。
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait AsyncEntityReadableByOption[CTX <: EntityIOContext[Future], ID <: Identity[_], E <: Entity[ID]]
  extends EntityReadableByOption[CTX, ID, E, Future] {
  this: AsyncEntityReader[CTX, ID, E] =>

  /**
   * @return Success:
   *         Option[エンティティ]
   *         Failure:
   *         Futureが失敗した場合の例外
   */
  def resolveOption(identity: ID)(implicit ctx: CTX): Future[Option[E]]

}
