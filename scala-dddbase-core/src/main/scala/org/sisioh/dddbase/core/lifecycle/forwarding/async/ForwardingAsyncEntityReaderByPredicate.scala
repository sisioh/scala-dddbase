package org.sisioh.dddbase.core.lifecycle.forwarding.async

import org.sisioh.dddbase.core.lifecycle.{EntityIOContext, EntitiesChunk}
import org.sisioh.dddbase.core.lifecycle.async.{AsyncEntityReadableByPredicate, AsyncEntityReader}
import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.concurrent.Future

/**
 * [[org.sisioh.dddbase.core.lifecycle.async.AsyncEntityReadableByPredicate]]のデコレータ。
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait ForwardingAsyncEntityReaderByPredicate[CTX <: EntityIOContext[Future] , ID <: Identity[_], E <: Entity[ID]]
  extends AsyncEntityReadableByPredicate[CTX, ID, E] {
  this: AsyncEntityReader[CTX, ID, E] =>

  type Delegate <: AsyncEntityReadableByPredicate[CTX, ID, E]

  /**
   * デリゲート。
   */
  protected val delegate: Delegate

  def filterByPredicate
  (predicate: (E) => Boolean,
   index: Option[Int], maxEntities: Option[Int])
  (implicit ctx: CTX): Future[EntitiesChunk[ID, E]] =
    delegate.filterByPredicate(predicate, index, maxEntities)

}
