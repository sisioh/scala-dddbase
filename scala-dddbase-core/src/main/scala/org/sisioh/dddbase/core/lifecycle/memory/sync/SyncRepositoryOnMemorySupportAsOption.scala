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
trait SyncRepositoryOnMemorySupportAsOption
[ID <: Identifier[_],
E <: Entity[ID] with EntityCloneable[ID, E] with Ordered[E]]
  extends SyncRepositoryOnMemorySupport[ID, E]
  with SyncEntityReadableAsOption[ID, E] {

  override def resolveAsOptionBy(identifier: ID)(implicit ctx: EntityIOContext[Try]) = synchronized {
    existBy(identifier).map {
      result =>
        if (result) {
          Some(entities(identifier).clone)
        } else {
          None
        }
    }.getOrElse(None)
  }

}
