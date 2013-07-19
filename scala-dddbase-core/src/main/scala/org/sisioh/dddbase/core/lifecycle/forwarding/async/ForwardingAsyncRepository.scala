package org.sisioh.dddbase.core.lifecycle.forwarding.async

import org.sisioh.dddbase.core.model.{Entity, Identity}
import org.sisioh.dddbase.core.lifecycle.async.{AsyncRepository, AsyncEntityWriter, AsyncEntityReader}

/**
 * [[org.sisioh.dddbase.core.lifecycle.async.AsyncRepository]]のデコレータ。
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait ForwardingAsyncRepository[ID <: Identity[_], E <: Entity[ID]]
  extends ForwardingAsyncEntityReader[ID, E]
  with ForwardingAsyncEntityWriter[ID, E]
  with AsyncRepository[ID, E]{

  /**
   * デリゲート。
   */
  protected val delegateAsyncRepository: AsyncRepository[ID, E]

  protected val delegateAsyncEntityReader: AsyncEntityReader[ID, E] = delegateAsyncRepository

  protected val delegateAsyncEntityWriter: AsyncEntityWriter[ID, E] = delegateAsyncRepository

}


