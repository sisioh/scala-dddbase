package org.sisioh.dddbase.core.lifecycle.forwarding.async

import org.sisioh.dddbase.core.lifecycle.{ResultWithEntity}
import org.sisioh.dddbase.core.lifecycle.async.{AsyncResultWithEntity, AsyncEntityWriter}
import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.concurrent.Future

trait ForwardingAsyncEntityWriter[ID <: Identity[_], E <: Entity[ID]]
  extends AsyncEntityWriter[ID, E] {

  protected val delegateAsyncEntityWriter: AsyncEntityWriter[ID, E]

  protected def createInstance(state: Future[(AsyncEntityWriter[ID, E], Option[E])]): Future[(This, Option[E])]

  def store(entity: E): Future[ResultWithEntity[This, ID, E, Future]] = {
    val state = delegateAsyncEntityWriter.store(entity).map {
      result =>
        (result.result.asInstanceOf[AsyncEntityWriter[ID, E]], Some(result.entity))
    }
    val instance = createInstance(state)
    instance.map {
      e =>
        AsyncResultWithEntity(e._1, e._2.get)
    }
  }

  def delete(identity: ID): Future[This] = {
    val state = delegateAsyncEntityWriter.delete(identity).map {
      result =>
        (result.asInstanceOf[AsyncEntityWriter[ID, E]], None)
    }
    val instance = createInstance(state)
    instance.map {
      e =>
        e._1
    }
  }

}
