package org.sisioh.dddbase.core.lifecycle.memory.async

import org.sisioh.dddbase.core.lifecycle.async._
import org.sisioh.dddbase.core.lifecycle.memory.sync.SyncRepositoryOnMemory
import org.sisioh.dddbase.core.lifecycle.sync.SyncEntityReadableByOption
import org.sisioh.dddbase.core.model._
import scala.concurrent._
import org.sisioh.dddbase.core.lifecycle.EntityIOContext
import scala.util.Try

/**
 * [org.sisioh.dddbase.core.lifecycle.memory.async.syncOnMemoryRepositorySupport]]にOption型のサポートを追加するトレイト。
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait AsyncRepositoryOnMemorySupportByOption
[CTX <: EntityIOContext[Future], ID <: Identity[_], E <: Entity[ID] with EntityCloneable[ID, E]]
  extends AsyncRepositoryOnMemorySupport[CTX, ID, E]
  with AsyncEntityReadableByOption[CTX, ID, E] {

  type Delegate <: SyncRepositoryOnMemory[EntityIOContext[Try], ID, E] with SyncEntityReadableByOption[EntityIOContext[Try], ID, E]

  def resolveOption(identifier: ID)(implicit ctx: CTX) = {
    val asyncCtx = getAsyncWrappedEntityIOContext(ctx)
    implicit val executor = asyncCtx.executor
    future {
      implicit val syncCtx = asyncCtx.syncEntityIOContext
      delegate.resolveOption(identifier).get
    }
  }

}
