package org.sisioh.dddbase.core.lifecycle.forwarding.async

import org.sisioh.dddbase.core.lifecycle.{EntityIOContext, EntitiesChunk}
import org.sisioh.dddbase.core.lifecycle.async.{AsyncEntityReadableByChunk, AsyncEntityReader}
import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.concurrent.Future

/**
 * [[org.sisioh.dddbase.core.lifecycle.async.AsyncEntityReadableByChunk]]のデコレータ。
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait ForwardingAsyncEntityReadableByChunk[ID <: Identity[_], E <: Entity[ID]]
  extends AsyncEntityReadableByChunk[ID, E] {
  this: AsyncEntityReader[ID, E] =>

  type Delegate <: AsyncEntityReadableByChunk[ID, E]

  /**
   * デリゲート。
   */
  protected val delegate: Delegate

  def resolveChunk(index: Int, maxEntities: Int)
                  (implicit ctx: EntityIOContext[Future]): Future[EntitiesChunk[ID, E]] =
    delegate.resolveChunk(index, maxEntities)

}
