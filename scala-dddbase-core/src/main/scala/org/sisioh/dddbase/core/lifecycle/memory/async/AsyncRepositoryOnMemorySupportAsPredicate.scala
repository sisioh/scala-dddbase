package org.sisioh.dddbase.core.lifecycle.memory.async

import org.sisioh.dddbase.core.lifecycle.{EntityIOContext, EntitiesChunk}
import org.sisioh.dddbase.core.lifecycle.async._
import org.sisioh.dddbase.core.lifecycle.sync.SyncEntityReadableAsPredicate
import org.sisioh.dddbase.core.model._
import scala.concurrent._
import org.sisioh.dddbase.core.lifecycle.memory.sync.SyncRepositoryOnMemory
import scala.util.Success

/**
 * [[org.sisioh.dddbase.core.lifecycle.memory.async.AsyncRepositoryOnMemorySupport]]に
 * [[org.sisioh.dddbase.core.lifecycle.async.AsyncEntityReadableAsPredicate]]ための機能を追加するトレイト。
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait AsyncRepositoryOnMemorySupportAsPredicate
[ID <: Identifier[_], E <: Entity[ID] with EntityCloneable[ID, E]]
  extends AsyncEntityReadableAsPredicate[ID, E] {
  this: AsyncRepositoryOnMemory[ID, E] =>

  def filterBy
  (predicate: (E) => Boolean, indexOpt: Option[Int], maxEntitiesOpt: Option[Int])
  (implicit ctx: EntityIOContext[Future])
  : Future[EntitiesChunk[ID, E]] = {
    implicit val executor = getExecutionContext(ctx)
    Future {
      val filteredSubEntities = getEntities.values.toList.filter(predicate)
      val index = indexOpt.getOrElse(0)
      val maxEntities = maxEntitiesOpt.getOrElse(filteredSubEntities.size)
      val subEntities = filteredSubEntities.slice(index * maxEntities, index * maxEntities + maxEntities)
      EntitiesChunk(index, subEntities)
    }
  }

}
