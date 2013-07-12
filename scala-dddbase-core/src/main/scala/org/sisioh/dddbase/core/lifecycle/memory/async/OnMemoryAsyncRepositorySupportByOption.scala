package org.sisioh.dddbase.core.lifecycle.memory.async

import org.sisioh.dddbase.core.lifecycle.async._
import org.sisioh.dddbase.core.lifecycle.sync.SyncEntityReaderByOption
import org.sisioh.dddbase.core.model._
import scala.concurrent._
import org.sisioh.dddbase.core.lifecycle.memory.sync.OnMemorySyncRepository

/**
 * [org.sisioh.dddbase.core.lifecycle.memory.async.syncOnMemoryRepositorySupport]]にOption型のサポートを追加するトレイト。
 *
 * @tparam AR 当該リポジトリを実装する派生型
 * @tparam SR 内部で利用する同期型リポジトリの型
 * @tparam ID 識別子の型
 * @tparam T エンティティの型
 */
trait OnMemoryAsyncRepositorySupportByOption
[+AR <: AsyncRepository[_, ID, T],
SR <: OnMemorySyncRepository[_, ID, T] with SyncEntityReaderByOption[ID, T],
ID <: Identity[_],
T <: Entity[ID] with EntityCloneable[ID, T]]
  extends OnMemoryAsyncRepositorySupport[AR, SR, ID, T]
  with AsyncEntityReaderByOption[ID, T] {

  def resolveOption(identifier: ID)(implicit executor: ExecutionContext) = future {
    core.resolveOption(identifier).get
  }

}
