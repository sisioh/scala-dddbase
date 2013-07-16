package org.sisioh.dddbase.core.lifecycle.memory.async

import org.sisioh.dddbase.core.lifecycle.EntitiesChunk
import org.sisioh.dddbase.core.lifecycle.async._
import org.sisioh.dddbase.core.lifecycle.sync.SyncEntityReaderByPredicate
import org.sisioh.dddbase.core.model._
import scala.concurrent._
import org.sisioh.dddbase.core.lifecycle.memory.sync.SyncRepositoryOnMemory

/**
 * [[org.sisioh.dddbase.core.lifecycle.memory.async.AsyncRepositoryOnMemorySupport]]に
 * [[org.sisioh.dddbase.core.lifecycle.async.AsyncEntityReaderByPredicate]]ための機能を追加するトレイト。
 *
 * @tparam AR 当該リポジトリを実装する派生型
 * @tparam SR 内部で利用する同期型リポジトリの型
 * @tparam ID 識別子の型
 * @tparam T エンティティの型
 */
trait AsyncRepositoryOnMemorySupportByPredicate
[+AR <: AsyncRepository[_, ID, T],
SR <: SyncRepositoryOnMemory[_, ID, T] with SyncEntityReaderByPredicate[ID, T],
ID <: Identity[_],
T <: Entity[ID] with EntityCloneable[ID, T]]
  extends AsyncRepositoryOnMemorySupport[AR, SR, ID, T]
  with AsyncEntityReaderByPredicate[ID, T] {

  def filterByPredicate
  (predicate: (T) => Boolean, index: Option[Int], maxEntities: Option[Int]): Future[EntitiesChunk[ID, T]] = future {
    core.filterByPredicate(predicate, index, maxEntities).get
  }

}
