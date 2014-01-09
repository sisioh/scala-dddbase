package org.sisioh.dddbase.core.lifecycle.memory.mutable.sync

import org.sisioh.dddbase.core.lifecycle.{EntityIOContext, EntitiesChunk}
import org.sisioh.dddbase.core.lifecycle.sync._
import org.sisioh.dddbase.core.model._
import scala.util._

/**
 * [[org.sisioh.dddbase.core.lifecycle.memory.mutable.sync.SyncRepositoryOnMemorySupport]]に
 * [[org.sisioh.dddbase.core.lifecycle.sync.SyncEntityReadableAsPredicate]]ための機能を追加するトレイト。
 *
 * @tparam ID エンティティの識別子の型
 * @tparam E エンティティの型
 */
trait SyncRepositoryOnMemorySupportAsPredicate
[ID <: Identifier[_],
E <: Entity[ID] with EntityCloneable[ID, E] with Ordered[E]]
  extends SyncRepositoryOnMemorySupport[ID, E]
  with SyncEntityReadableAsPredicate[ID, E] {

  def filterBy
  (predicate: (E) => Boolean, indexOpt: Option[Int], maxEntitiesOpt: Option[Int])
  (implicit ctx: EntityIOContext[Try]): Try[EntitiesChunk[ID, E]] = {
    val filteredSubEntities = toList.filter(predicate)
    val index = indexOpt.getOrElse(0)
    val maxEntities = maxEntitiesOpt.getOrElse(filteredSubEntities.size)
    val subEntities = filteredSubEntities.slice(index * maxEntities, index * maxEntities + maxEntities)
    Success(EntitiesChunk(index, subEntities))
  }

}
