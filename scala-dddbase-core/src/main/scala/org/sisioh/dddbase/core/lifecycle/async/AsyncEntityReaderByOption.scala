package org.sisioh.dddbase.core.lifecycle.async

import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.concurrent.Future
import org.sisioh.dddbase.core.lifecycle.EntityReaderByOption

/**
 * エンティティを`Option`でラップして返すための[[org.sisioh.dddbase.core.lifecycle.async.AsyncEntityReader]]。
 *
 * @tparam ID 識別子の型
 * @tparam T エンティティの型
 */
trait AsyncEntityReaderByOption[ID <: Identity[_], T <: Entity[ID]]
  extends EntityReaderByOption[ID, T, Future] {
  this: AsyncEntityReader[ID, T] =>

  /**
   * 識別子に該当するエンティティを解決する。
   *
   * @see [[org.sisioh.dddbase.core.lifecycle.EntityReader]] `resolve`
   *
   * @param identity 識別子
   * @return Success:
   *         Option[エンティティ]
   *         Failure:
   *         Futureが失敗した場合の例外
   */
  def resolveOption(identity: ID): Future[Option[T]]

}
