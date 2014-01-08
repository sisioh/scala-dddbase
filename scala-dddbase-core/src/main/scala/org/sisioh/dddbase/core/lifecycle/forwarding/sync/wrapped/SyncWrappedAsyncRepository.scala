package org.sisioh.dddbase.core.lifecycle.forwarding.sync.wrapped

import org.sisioh.dddbase.core.model.{Entity, Identifier}
import org.sisioh.dddbase.core.lifecycle.async.AsyncRepository
import org.sisioh.dddbase.core.lifecycle.sync.SyncRepository

/**
 * [[org.sisioh.dddbase.core.lifecycle.async.AsyncRepository]]を
 * [[org.sisioh.dddbase.core.lifecycle.sync.SyncRepository]]として
 * ラップするためのデコレータ。
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait SyncWrappedAsyncRepository[ID <: Identifier[_], E <: Entity[ID]]
  extends SyncWrappedAsyncEntityReader[ID, E]
  with SyncWrappedAsyncEntityWriter[ID, E]
  with SyncRepository[ID, E] {

  type Delegate <: AsyncRepository[ID, E]

}
