package org.sisioh.dddbase.core.lifecycle.forwarding.async.wrapped

import org.sisioh.dddbase.core.lifecycle.async.AsyncRepository
import org.sisioh.dddbase.core.lifecycle.sync.SyncRepository
import org.sisioh.dddbase.core.model.{Entity, Identifier}

/**
 * [[org.sisioh.dddbase.core.lifecycle.sync.SyncRepository]]を
 * [[org.sisioh.dddbase.core.lifecycle.async.AsyncRepository]]として
 * ラップするためのデコレータ。
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait AsyncWrappedSyncRepository[ID <: Identifier[_], E <: Entity[ID]]
  extends AsyncWrappedSyncEntityReader[ID, E]
  with AsyncWrappedSyncEntityWriter[ID, E]
  with AsyncRepository[ID, E] {

  type Delegate <: SyncRepository[ID, E]

}
