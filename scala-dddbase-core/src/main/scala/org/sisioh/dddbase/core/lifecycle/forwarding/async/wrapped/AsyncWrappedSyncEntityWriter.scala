package org.sisioh.dddbase.core.lifecycle.forwarding.async.wrapped

import org.sisioh.dddbase.core.lifecycle.async.{AsyncResultWithEntity, AsyncEntityWriter}
import org.sisioh.dddbase.core.lifecycle.sync.SyncEntityWriter
import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.concurrent._
import org.sisioh.dddbase.core.lifecycle.EntityIOContext

/**
 * [[org.sisioh.dddbase.core.lifecycle.sync.SyncEntityWriter]]を
 * [[org.sisioh.dddbase.core.lifecycle.async.AsyncEntityWriter]]として
 * ラップするためのデコレータ。
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait AsyncWrappedSyncEntityWriter[ID <: Identity[_], E <: Entity[ID]]
  extends AsyncEntityWriter[ID, E] with AsyncWrappedSyncEntityIO {

  type Delegate <: SyncEntityWriter[ID, E]

  /**
   * デリゲート。
   */
  protected val delegate: Delegate

  protected def createInstance(state: (Delegate#This, Option[E])): (This, Option[E])

  def storeEntity(entity: E)(implicit ctx: Ctx): Future[AsyncResultWithEntity[This, ID, E]] = {
    val asyncCtx = getAsyncWrappedEntityIOContext(ctx)
    implicit val executor = asyncCtx.executor
    future {
      implicit val syncCtx = asyncCtx.syncEntityIOContext
      val resultWithEntity = delegate.storeEntity(entity).get
      val _entity: Option[E] = Some(resultWithEntity.entity.asInstanceOf[E])
      val result = createInstance((resultWithEntity.result.asInstanceOf[Delegate#This], _entity) )
      AsyncResultWithEntity[This, ID, E](result._1.asInstanceOf[This], result._2.get)
    }
  }

  def deleteByIdentifier(identity: ID)(implicit ctx: Ctx): Future[AsyncResultWithEntity[This, ID, E]] = {
    val asyncCtx = getAsyncWrappedEntityIOContext(ctx)
    implicit val executor = asyncCtx.executor
    future {
      implicit val syncCtx = asyncCtx.syncEntityIOContext
      val resultWithEntity = delegate.deleteByIdentifier(identity).get
      val _entity: Option[E] = Some(resultWithEntity.entity.asInstanceOf[E])
      val result = createInstance((resultWithEntity.result.asInstanceOf[Delegate#This], _entity))
      AsyncResultWithEntity[This, ID, E](result._1.asInstanceOf[This], result._2.get)
    }
  }

}
