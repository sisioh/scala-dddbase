package org.sisioh.dddbase.core.lifecycle.memory.mutable.sync

import org.sisioh.dddbase.core.lifecycle.EntityNotFoundException
import org.sisioh.dddbase.core.lifecycle.sync._
import org.sisioh.dddbase.core.model._
import scala.util._

/**
 * [[org.sisioh.dddbase.core.lifecycle.memory.mutable.sync.SyncRepositoryOnMemory]]にOption型のサポートを追加するトレイト。
 *
 * @tparam ID エンティティの識別子の型
 * @tparam E エンティティの型
 */
trait SyncRepositoryOnMemorySupportAsOption
[ID <: Identifier[_],
E <: Entity[ID] with EntityCloneable[ID, E] with Ordered[E]]
  extends SyncEntityReadableAsOption[ID, E] {
  this: SyncRepositoryOnMemory[ID, E] =>

  def resolveAsOptionBy(identifier: ID)(implicit ctx: Ctx): Option[E] = synchronized {
    resolveBy(identifier).map(Some(_)).recoverWith {
      case ex: EntityNotFoundException =>
        Success(None)
    }.getOrElse(None)
  }

}
