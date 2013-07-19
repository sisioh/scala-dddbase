package org.sisioh.dddbase.core.lifecycle.memory.async

import org.sisioh.dddbase.core.lifecycle.async._
import org.sisioh.dddbase.core.lifecycle.sync.SyncEntityReaderByOption
import org.sisioh.dddbase.core.model._
import scala.concurrent._
import org.sisioh.dddbase.core.lifecycle.memory.sync.SyncRepositoryOnMemory

/**
 * [org.sisioh.dddbase.core.lifecycle.memory.async.syncOnMemoryRepositorySupport]]にOption型のサポートを追加するトレイト。
 *
 * @tparam SR 内部で利用する同期型リポジトリの型
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait AsyncRepositoryOnMemorySupportByOption
[SR <: SyncRepositoryOnMemory[ID, E] with SyncEntityReaderByOption[ID, E],
ID <: Identity[_],
E <: Entity[ID] with EntityCloneable[ID, E]]
  extends AsyncRepositoryOnMemorySupport[SR, ID, E]
  with AsyncEntityReaderByOption[ID, E] {

  def resolveOption(identifier: ID) = future {
    core.resolveOption(identifier).get
  }

}
