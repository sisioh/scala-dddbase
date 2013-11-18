package org.sisioh.dddbase.core.lifecycle.forwarding.async

import org.sisioh.dddbase.core.lifecycle.async.{AsyncEntityReadableByOption, AsyncEntityReader}
import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.concurrent.Future
import org.sisioh.dddbase.core.lifecycle.EntityIOContext

/**
 * [[org.sisioh.dddbase.core.lifecycle.async.AsyncEntityReadableByOption]]のデコレータ実装。
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait ForwardingAsyncEntityReaderByOption[CTX <: EntityIOContext[Future], ID <: Identity[_], E <: Entity[ID]]
  extends AsyncEntityReadableByOption[CTX, ID, E] {
  this: AsyncEntityReader[CTX, ID, E] =>

  type Delegate <: AsyncEntityReadableByOption[CTX, ID, E]

  protected val delegate: Delegate

  def resolveOption(identity: ID)(implicit ctx: CTX): Future[Option[E]] =
    delegate.resolveOption(identity)

}
