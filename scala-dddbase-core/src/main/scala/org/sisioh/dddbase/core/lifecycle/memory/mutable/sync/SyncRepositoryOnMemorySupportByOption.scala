package org.sisioh.dddbase.core.lifecycle.memory.mutable.sync

import org.sisioh.dddbase.core.lifecycle.{EntityIOContext, EntityNotFoundException}
import org.sisioh.dddbase.core.lifecycle.sync._
import org.sisioh.dddbase.core.model._
import scala.util._

/**
 * [[org.sisioh.dddbase.core.lifecycle.memory.mutable.sync.SyncRepositoryOnMemorySupport]]にOption型のサポートを追加するトレイト。
 *
 * @tparam ID エンティティの識別子の型
 * @tparam E エンティティの型
 */
trait SyncRepositoryOnMemorySupportByOption
[CTX <: EntityIOContext[Try] , ID <: Identity[_],
E <: Entity[ID] with EntityCloneable[ID, E] with Ordered[E]]
  extends SyncRepositoryOnMemorySupport[CTX, ID, E]
  with SyncEntityReadableByOption[CTX, ID, E] {

  def resolveOption(identity: ID)(implicit ctx: CTX): Try[Option[E]] = synchronized {
    resolve(identity).map(Some(_)).recoverWith {
      case ex: EntityNotFoundException =>
        Success(None)
    }
  }

}
