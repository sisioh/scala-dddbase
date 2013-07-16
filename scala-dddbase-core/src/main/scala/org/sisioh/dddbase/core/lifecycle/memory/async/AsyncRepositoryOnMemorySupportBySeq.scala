package org.sisioh.dddbase.core.lifecycle.memory.async

import org.sisioh.dddbase.core.lifecycle.async._
import org.sisioh.dddbase.core.lifecycle.sync.SyncEntityReaderByOption
import org.sisioh.dddbase.core.model._
import scala.concurrent._
import org.sisioh.dddbase.core.lifecycle.memory.sync.SyncRepositoryOnMemory

/**
 * [[org.sisioh.dddbase.core.lifecycle.memory.async.AsyncRepositoryOnMemorySupport]]に全件取得のための機能を追加するトレイト。
 *
 * @tparam SR 内部で利用する同期型リポジトリの型
 * @tparam ID 識別子の型
 * @tparam T エンティティの型
 */
trait AsyncRepositoryOnMemorySupportBySeq
[SR <: SyncRepositoryOnMemory[ID, T] with SyncEntityReaderByOption[ID, T],
ID <: Identity[_],
T <: Entity[ID] with EntityCloneable[ID, T]]
  extends AsyncRepositoryOnMemorySupport[SR, ID, T] with AsyncEntityReaderBySeq[ID, T] {

  def resolveAll: Future[Seq[T]] = future {
    core.toSeq
  }

}
