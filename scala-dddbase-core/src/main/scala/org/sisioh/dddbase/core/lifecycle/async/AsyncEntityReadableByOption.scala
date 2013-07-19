package org.sisioh.dddbase.core.lifecycle.async

import org.sisioh.dddbase.core.lifecycle.EntityReadableByOption
import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.concurrent.Future

/**
 * エンティティを`Option`でラップして返すための[[org.sisioh.dddbase.core.lifecycle.async.AsyncEntityReader]]。
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait AsyncEntityReadableByOption[ID <: Identity[_], E <: Entity[ID]]
  extends EntityReadableByOption[ID, E, Future] {
  this: AsyncEntityReader[ID, E] =>

  /**
   * @return Success:
   *         Option[エンティティ]
   *         Failure:
   *         Futureが失敗した場合の例外
   */
  def resolveOption(identity: ID): Future[Option[E]]

}
