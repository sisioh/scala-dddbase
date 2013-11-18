package org.sisioh.dddbase.core.lifecycle.forwarding.sync.wrapped

import org.sisioh.dddbase.core.model.{Entity, Identity}
import org.sisioh.dddbase.core.lifecycle.async.AsyncRepository
import org.sisioh.dddbase.core.lifecycle.sync.SyncRepository
import scala.util.Try
import org.sisioh.dddbase.core.lifecycle.EntityIOContext
import scala.concurrent.Future

/**
 * [[org.sisioh.dddbase.core.lifecycle.async.AsyncRepository]]を
 * [[org.sisioh.dddbase.core.lifecycle.sync.SyncRepository]]として
 * ラップするためのデコレータ。
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait SyncWrappedAsyncRepository[CTX <: EntityIOContext[Try], ID <: Identity[_], E <: Entity[ID]]
  extends SyncWrappedAsyncEntityReader[CTX, ID, E]
  with SyncWrappedAsyncEntityWriter[CTX, ID, E]
  with SyncRepository[CTX, ID, E] {

  type Delegate <: AsyncRepository[EntityIOContext[Future], ID, E]

}
