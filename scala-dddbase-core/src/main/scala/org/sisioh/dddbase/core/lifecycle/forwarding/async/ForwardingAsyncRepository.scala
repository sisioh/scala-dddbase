package org.sisioh.dddbase.core.lifecycle.forwarding.async

import org.sisioh.dddbase.core.lifecycle.async.AsyncRepository
import org.sisioh.dddbase.core.model.{Entity, Identity}

/**
 * [[org.sisioh.dddbase.core.lifecycle.async.AsyncRepository]]のデコレータ。
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait ForwardingAsyncRepository[ID <: Identity[_], E <: Entity[ID]]
  extends ForwardingAsyncEntityReader[ID, E]
  with ForwardingAsyncEntityWriter[ID, E]
  with AsyncRepository[ID, E] {

  type Delegate <: AsyncRepository[ID, E]

  /**
   * デリゲート。
   */
  protected val delegate: Delegate


}


