package org.sisioh.dddbase.core.lifecycle.forwarding.async

import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.concurrent.{Future, ExecutionContext}
import org.sisioh.dddbase.core.lifecycle.async.AsyncEntityReader

/**
 * [[org.sisioh.dddbase.core.lifecycle.async.AsyncEntityReader]]のデコレータ。
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait ForwardingAsyncEntityReader[ID <: Identity[_], E <: Entity[ID]] extends AsyncEntityReader[ID, E] {

  /**
   * デリゲート。
   */
  protected val delegateAsyncEntityReader: AsyncEntityReader[ID, E]

  def resolve(identity: ID) =
    delegateAsyncEntityReader.resolve(identity)

  def contains(identity: ID): Future[Boolean] =
    delegateAsyncEntityReader.contains(identity)

}
