package org.sisioh.dddbase.core.lifecycle.memory.async

import org.sisioh.dddbase.core.lifecycle.EntitiesChunk
import org.sisioh.dddbase.core.lifecycle.async._
import org.sisioh.dddbase.core.lifecycle.sync.SyncEntityReaderByChunk
import org.sisioh.dddbase.core.model._
import scala.concurrent._
import org.sisioh.dddbase.core.lifecycle.memory.sync.OnMemorySyncRepository

/**
 * [[org.sisioh.dddbase.core.lifecycle.memory.async.OnMemoryAsyncRepositorySupport]]に
 * [[org.sisioh.dddbase.core.lifecycle.EntitiesChunk]]のための機能を追加するトレイト。
 *
 * @tparam AR 当該リポジトリを実装する派生型
 * @tparam SR 内部で利用する同期型リポジトリの型
 * @tparam ID 識別子の型
 * @tparam T エンティティの型
 */
trait OnMemoryAsyncRepositorySupportByChunk
[+AR <: AsyncRepository[_, ID, T],
SR <: OnMemorySyncRepository[_, ID, T] with SyncEntityReaderByChunk[ID, T],
ID <: Identity[_],
T <: Entity[ID] with EntityCloneable[ID, T]]
  extends OnMemoryAsyncRepositorySupport[AR, SR, ID, T]
  with AsyncEntityReaderByChunk[ID, T] {

  def resolveChunk(index: Int, maxEntities: Int)(implicit executor: ExecutionContext): Future[EntitiesChunk[ID, T]] = future {
    core.resolveChunk(index, maxEntities).get
  }

}
