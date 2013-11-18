package org.sisioh.dddbase.core.lifecycle.memory.sync

import org.sisioh.dddbase.core.lifecycle.{EntityIOContext, EntitiesChunk}
import org.sisioh.dddbase.core.lifecycle.sync._
import org.sisioh.dddbase.core.model._
import scala.util._

/**
 * [[org.sisioh.dddbase.core.lifecycle.memory.sync.SyncRepositoryOnMemorySupport]]に
 * [[org.sisioh.dddbase.core.lifecycle.EntitiesChunk]]ための機能を追加するトレイト。
 *
 * @tparam ID エンティティの識別子の型
 * @tparam E エンティティの型
 */
trait SyncRepositoryOnMemorySupportByChunk
[CTX <: EntityIOContext[Try], ID <: Identity[_],
E <: Entity[ID] with EntityCloneable[ID, E] with Ordered[E]]
  extends SyncRepositoryOnMemorySupport[CTX, ID, E] with SyncEntityReadableByChunk[CTX, ID, E] {

  def resolveChunk(index: Int, maxEntities: Int)(implicit ctx: CTX): Try[EntitiesChunk[ID, E]] = {
    val subEntities = toList.slice(index * maxEntities, index * maxEntities + maxEntities)
    Success(EntitiesChunk(index, subEntities))
  }

}
