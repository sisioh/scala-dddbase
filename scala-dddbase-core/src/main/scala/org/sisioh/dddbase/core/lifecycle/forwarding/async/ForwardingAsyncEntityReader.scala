package org.sisioh.dddbase.core.lifecycle.forwarding.async

import org.sisioh.dddbase.core.lifecycle.async.AsyncEntityReader
import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.concurrent.Future

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

  def resolve(identity: ID) =
    delegate.resolve(identity)

  def contains(identity: ID): Future[Boolean] =
    delegate.contains(identity)

}
