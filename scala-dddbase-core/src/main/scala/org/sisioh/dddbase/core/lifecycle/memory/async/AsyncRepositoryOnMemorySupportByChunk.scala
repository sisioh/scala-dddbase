package org.sisioh.dddbase.core.lifecycle.memory.async

import org.sisioh.dddbase.core.lifecycle.EntitiesChunk
import org.sisioh.dddbase.core.lifecycle.async._
import org.sisioh.dddbase.core.lifecycle.memory.sync.SyncRepositoryOnMemory
import org.sisioh.dddbase.core.lifecycle.sync.SyncEntityReaderByChunk
import org.sisioh.dddbase.core.model._
import scala.concurrent._

/**
 * [[org.sisioh.dddbase.core.lifecycle.memory.async.AsyncRepositoryOnMemorySupport]]に
 * [[org.sisioh.dddbase.core.lifecycle.EntitiesChunk]]のための機能を追加するトレイト。
 *
 * @tparam SR 内部で利用する同期型リポジトリの型
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait AsyncRepositoryOnMemorySupportByChunk
[SR <: SyncRepositoryOnMemory[ID, E] with SyncEntityReaderByChunk[ID, E],
ID <: Identity[_],
E <: Entity[ID] with EntityCloneable[ID, E]]
  extends AsyncRepositoryOnMemorySupport[SR, ID, E]
  with AsyncEntityReaderByChunk[ID, E] {

  def resolveChunk(index: Int, maxEntities: Int): Future[EntitiesChunk[ID, E]] = future {
    core.resolveChunk(index, maxEntities).get
  }

}
