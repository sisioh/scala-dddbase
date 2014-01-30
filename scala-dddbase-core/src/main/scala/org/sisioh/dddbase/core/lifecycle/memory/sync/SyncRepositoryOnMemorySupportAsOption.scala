package org.sisioh.dddbase.core.lifecycle.memory.sync

import org.sisioh.dddbase.core.lifecycle.{EntityNotFoundException, EntityIOContext}
import org.sisioh.dddbase.core.lifecycle.sync._
import org.sisioh.dddbase.core.model._
import scala.Some
import scala.util._

/**
 * [[org.sisioh.dddbase.core.lifecycle.memory.sync.SyncRepositoryOnMemorySupport]]にOption型のサポートを追加するトレイト。
 *
 * @tparam ID エンティティの識別子の型
 * @tparam E エンティティの型
 */
trait SyncRepositoryOnMemorySupportAsOption
[ID <: Identifier[_],
E <: Entity[ID] with EntityCloneable[ID, E] with Ordered[E]]
  extends SyncEntityReadableAsOption[ID, E] {
  this: SyncRepositoryOnMemory[ID, E] =>

  override def resolveAsOptionBy(identifier: ID)(implicit ctx: Ctx) = synchronized {
    resolveBy(identifier).map(Some(_)).recoverWith {
      case ex: EntityNotFoundException =>
        Success(None)
    }.getOrElse(None)
  }

}
