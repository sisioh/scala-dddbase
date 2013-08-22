package org.sisioh.dddbase.core.lifecycle.forwarding.sync.wrapped

import org.sisioh.dddbase.core.model.{Entity, Identity}
import org.sisioh.dddbase.core.lifecycle.sync.{SyncResultWithEntity, SyncEntityWriter}
import org.sisioh.dddbase.core.lifecycle.async.AsyncEntityWriter
import scala.util.Try
import scala.concurrent.duration.Duration
import scala.concurrent.Await
import org.sisioh.dddbase.core.lifecycle.EntityIOContext

/**
 * [[org.sisioh.dddbase.core.lifecycle.async.AsyncEntityWriter]]を
 * [[org.sisioh.dddbase.core.lifecycle.sync.SyncEntityWriter]]として
 * ラップするためのデコレータ。
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait SyncWrappedAsyncEntityWriter[ID <: Identity[_], E <: Entity[ID]]
  extends SyncEntityWriter[ID, E] with SyncWrappedAsyncEntityIO {

  type Delegate <: AsyncEntityWriter[ID, E]

  /**
   * デリゲート。
   */
  protected val delegate: Delegate

  protected val timeOut: Duration

  protected def createInstance(state: (Delegate#This, Option[E])): (This, Option[E])

  def store(entity: E)(implicit ctx: EntityIOContext[Try]): Try[SyncResultWithEntity[This, ID, E]] = Try {
    implicit val asyncEntityIOContext =  getAsyncEntityIOContext(ctx)
    val resultWithEntity = Await.result(delegate.store(entity), timeOut)
    val result = createInstance((resultWithEntity.result.asInstanceOf[Delegate#This], Some(resultWithEntity.entity)))
    SyncResultWithEntity[This, ID, E](result._1.asInstanceOf[This], result._2.get)
  }

  def deleteByIdentity(identity: ID)(implicit ctx: EntityIOContext[Try]): Try[SyncResultWithEntity[This, ID, E]] = Try {
    implicit val asyncEntityIOContext =  getAsyncEntityIOContext(ctx)
    val resultWithEntity = Await.result(delegate.deleteByIdentity(identity), timeOut)
    val result = createInstance((resultWithEntity.result.asInstanceOf[Delegate#This], Some(resultWithEntity.entity)))
    SyncResultWithEntity[This, ID, E](result._1.asInstanceOf[This], result._2.get)
  }

}
