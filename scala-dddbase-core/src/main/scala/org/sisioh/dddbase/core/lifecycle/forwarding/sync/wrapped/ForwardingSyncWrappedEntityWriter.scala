package org.sisioh.dddbase.core.lifecycle.forwarding.sync.wrapped

import org.sisioh.dddbase.core.model.{Entity, Identity}
import org.sisioh.dddbase.core.lifecycle.sync.{SyncResultWithEntity, SyncEntityWriter}
import org.sisioh.dddbase.core.lifecycle.async.AsyncEntityWriter
import scala.util.Try
import scala.concurrent.duration.Duration
import scala.concurrent.Await

trait ForwardingSyncWrappedEntityWriter[ID <: Identity[_], E <: Entity[ID]]
  extends SyncEntityWriter[ID, E] {

  type Delegate <: AsyncEntityWriter[ID, E]

  /**
   * デリゲート。
   */
  protected val delegate: Delegate

  protected val timeOut: Duration

  protected def createInstance(state: (Delegate#This, Option[E])): (This, Option[E])

  def store(entity: E): Try[SyncResultWithEntity[This, ID, E]] = Try {
    val resultWithEntity = Await.result(delegate.store(entity), timeOut)
    val result = createInstance((resultWithEntity.result.asInstanceOf[Delegate#This], Some(resultWithEntity.entity)))
    SyncResultWithEntity[This, ID, E](result._1.asInstanceOf[This], result._2.get)
  }

  def delete(identity: ID): Try[SyncResultWithEntity[This, ID, E]] = Try {
    val resultWithEntity = Await.result(delegate.delete(identity), timeOut)
    val result = createInstance((resultWithEntity.result.asInstanceOf[Delegate#This], Some(resultWithEntity.entity)))
    SyncResultWithEntity[This, ID, E](result._1.asInstanceOf[This], result._2.get)
  }

}
