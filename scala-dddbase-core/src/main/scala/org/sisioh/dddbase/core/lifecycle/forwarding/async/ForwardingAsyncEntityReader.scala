package org.sisioh.dddbase.core.lifecycle.forwarding.async

import org.sisioh.dddbase.core.lifecycle.async.AsyncEntityReader
import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.concurrent.Future
import org.sisioh.dddbase.core.lifecycle.EntityIOContext

/**
 * [[org.sisioh.dddbase.core.lifecycle.async.AsyncEntityReader]]のデコレータ。
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait ForwardingAsyncEntityReader[ID <: Identity[_], E <: Entity[ID]]
  extends AsyncEntityReader[ID, E] {

  type Delegate <: AsyncEntityReader[ID, E]

  /**
   * デリゲート。
   */
  protected val delegate: Delegate

  def resolveBy(identifier: ID)(implicit ctx: EntityIOContext[Future]) =
    delegate.resolveBy(identifier)

  def existBy(identifier: ID)(implicit ctx: EntityIOContext[Future]): Future[Boolean] =
    delegate.existBy(identifier)

}
