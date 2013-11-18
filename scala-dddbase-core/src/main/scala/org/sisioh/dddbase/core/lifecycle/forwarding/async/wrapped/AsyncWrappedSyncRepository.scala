package org.sisioh.dddbase.core.lifecycle.forwarding.async.wrapped

import org.sisioh.dddbase.core.lifecycle.async.AsyncRepository
import org.sisioh.dddbase.core.lifecycle.sync.SyncRepository
import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.concurrent.Future
import org.sisioh.dddbase.core.lifecycle.EntityIOContext
import scala.util.Try

/**
 * [[org.sisioh.dddbase.core.lifecycle.sync.SyncRepository]]を
 * [[org.sisioh.dddbase.core.lifecycle.async.AsyncRepository]]として
 * ラップするためのデコレータ。
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait AsyncWrappedSyncRepository[CTX <: EntityIOContext[Future],ID <: Identity[_], E <: Entity[ID]]
  extends AsyncWrappedSyncEntityReader[CTX, ID, E]
  with AsyncWrappedSyncEntityWriter[CTX, ID, E]
  with AsyncRepository[CTX, ID, E] {

  type Delegate <: SyncRepository[EntityIOContext[Try], ID, E]

}
