package org.sisioh.dddbase.core.lifecycle.memory.async

import org.sisioh.dddbase.core.lifecycle.{EntityIOContext, EntitiesChunk}
import org.sisioh.dddbase.core.lifecycle.async._
import org.sisioh.dddbase.core.lifecycle.sync.SyncEntityReadableByPredicate
import org.sisioh.dddbase.core.model._
import scala.concurrent._
import org.sisioh.dddbase.core.lifecycle.memory.sync.SyncRepositoryOnMemory

/**
 * [[org.sisioh.dddbase.core.lifecycle.memory.async.AsyncRepositoryOnMemorySupport]]に
 * [[org.sisioh.dddbase.core.lifecycle.async.AsyncEntityReadableByPredicate]]ための機能を追加するトレイト。
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait AsyncRepositoryOnMemorySupportByPredicate
[ID <: Identity[_], E <: Entity[ID] with EntityCloneable[ID, E]]
  extends AsyncRepositoryOnMemorySupport[ID, E]
  with AsyncEntityReadableByPredicate[ID, E] {

  type Delegate <: SyncRepositoryOnMemory[ID, E] with SyncEntityReadableByPredicate[ID, E]

  def filterByPredicate
  (predicate: (E) => Boolean, index: Option[Int], maxEntities: Option[Int])
  (implicit ctx: EntityIOContext[Future])
  : Future[EntitiesChunk[ID, E]] = {
    val asyncCtx = getAsyncWrappedEntityIOContext(ctx)
    implicit val executor = asyncCtx.executor
    future {
      implicit val syncCtx = asyncCtx.syncEntityIOContext
      delegate.filterByPredicate(predicate, index, maxEntities).get
    }
  }

}
