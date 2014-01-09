package org.sisioh.dddbase.core.lifecycle.forwarding.async

import org.sisioh.dddbase.core.lifecycle.{EntityIOContext, EntitiesChunk}
import org.sisioh.dddbase.core.lifecycle.async.{AsyncEntityReadableAsChunk, AsyncEntityReader}
import org.sisioh.dddbase.core.model.{Entity, Identifier}
import scala.concurrent.Future

/**
 * [[org.sisioh.dddbase.core.lifecycle.async.AsyncEntityReadableAsChunk]]のデコレータ。
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait ForwardingAsyncEntityReadableAsChunk[ID <: Identifier[_], E <: Entity[ID]]
  extends AsyncEntityReadableAsChunk[ID, E] {
  this: AsyncEntityReader[ID, E] =>

  type Delegate <: AsyncEntityReadableAsChunk[ID, E]

  /**
   * デリゲート。
   */
  protected val delegate: Delegate

  def resolveAsChunk(index: Int, maxEntities: Int)
                  (implicit ctx: Ctx): Future[EntitiesChunk[ID, E]] =
    delegate.resolveAsChunk(index, maxEntities)

}
