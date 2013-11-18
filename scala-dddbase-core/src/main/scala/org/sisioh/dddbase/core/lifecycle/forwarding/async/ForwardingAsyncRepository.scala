package org.sisioh.dddbase.core.lifecycle.forwarding.async

import org.sisioh.dddbase.core.lifecycle.async.AsyncRepository
import org.sisioh.dddbase.core.model.{Entity, Identity}
import org.sisioh.dddbase.core.lifecycle.EntityIOContext
import scala.concurrent.Future

/**
 * [[org.sisioh.dddbase.core.lifecycle.async.AsyncRepository]]のデコレータ。
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait ForwardingAsyncRepository[CTX <: EntityIOContext[Future], ID <: Identity[_], E <: Entity[ID]]
  extends ForwardingAsyncEntityReader[CTX, ID, E]
  with ForwardingAsyncEntityWriter[CTX, ID, E]
  with AsyncRepository[CTX, ID, E] {

  type Delegate <: AsyncRepository[CTX, ID, E]

  /**
   * デリゲート。
   */
  protected val delegate: Delegate


}


