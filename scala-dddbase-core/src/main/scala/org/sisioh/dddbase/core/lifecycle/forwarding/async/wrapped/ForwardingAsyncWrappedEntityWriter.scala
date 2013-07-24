package org.sisioh.dddbase.core.lifecycle.forwarding.async.wrapped

import org.sisioh.dddbase.core.lifecycle.ResultWithEntity
import org.sisioh.dddbase.core.lifecycle.async.{AsyncResultWithEntity, AsyncEntityWriter}
import org.sisioh.dddbase.core.lifecycle.sync.SyncEntityWriter
import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.concurrent._

trait ForwardingAsyncWrappedEntityWriter[ID <: Identity[_], E <: Entity[ID]]
  extends AsyncEntityWriter[ID, E] {

  type Delegate <: SyncEntityWriter[ID, E]

  /**
   * デリゲート。
   */
  protected val delegate: Delegate

  protected def createInstance(state: (Delegate#This, Option[E])): (This, Option[E])

  def store(entity: E): Future[ResultWithEntity[This, ID, E, Future]] = future {
    val resultWithEntity = delegate.store(entity).get
    val result = createInstance((resultWithEntity.result.asInstanceOf[Delegate#This], Some(resultWithEntity.entity)))
    AsyncResultWithEntity[This, ID, E](result._1.asInstanceOf[This], result._2.get)
  }

  def delete(identity: ID): Future[This] = future {
    val resultTry = delegate.delete(identity).get
    val result = createInstance(resultTry.asInstanceOf[Delegate#This], None)
    result._1
  }

}
