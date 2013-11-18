package org.sisioh.dddbase.core.lifecycle.memory.sync

import org.sisioh.dddbase.core.lifecycle.sync._
import org.sisioh.dddbase.core.model._
import scala.Some
import scala.util._
import org.sisioh.dddbase.core.lifecycle.EntityIOContext

/**
 * [[org.sisioh.dddbase.core.lifecycle.memory.sync.SyncRepositoryOnMemorySupport]]にOption型のサポートを追加するトレイト。
 *
 * @tparam ID エンティティの識別子の型
 * @tparam E エンティティの型
 */
trait SyncRepositoryOnMemorySupportByOption
[CTX <: EntityIOContext[Try], ID <: Identity[_],
E <: Entity[ID] with EntityCloneable[ID, E] with Ordered[E]]
  extends SyncRepositoryOnMemorySupport[CTX, ID, E]
  with SyncEntityReadableByOption[CTX, ID, E] {

  override def resolveOption(identity: ID)(implicit ctx: CTX) = synchronized {
    containsByIdentity(identity).flatMap {
      _ =>
        Try {
          Some(entities(identity).clone)
        }.recoverWith {
          case ex: NoSuchElementException =>
            Success(None)
        }
    }
  }

}
