package org.sisioh.dddbase.core.lifecycle.forwarding.async

import org.sisioh.dddbase.core.lifecycle.{EntityIOContext, EntitiesChunk}
import org.sisioh.dddbase.core.lifecycle.async.{AsyncEntityReadableAsPredicate, AsyncEntityReader}
import org.sisioh.dddbase.core.model.{Entity, Identifier}
import scala.concurrent.Future

/**
 * [[org.sisioh.dddbase.core.lifecycle.async.AsyncEntityReadableAsPredicate]]のデコレータ。
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait ForwardingAsyncEntityReaderAsPredicate[ID <: Identifier[_], E <: Entity[ID]]
  extends AsyncEntityReadableAsPredicate[ID, E] {
  this: AsyncEntityReader[ID, E] =>

  type Delegate <: AsyncEntityReadableAsPredicate[ID, E]

  /**
   * デリゲート。
   */
  protected val delegate: Delegate

  def filterBy
  (predicate: (E) => Boolean,
   index: Option[Int], maxEntities: Option[Int])
  (implicit ctx: Ctx): Future[EntitiesChunk[ID, E]] =
    delegate.filterBy(predicate, index, maxEntities)

}
