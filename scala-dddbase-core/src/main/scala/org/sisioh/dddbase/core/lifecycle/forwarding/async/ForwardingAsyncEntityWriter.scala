package org.sisioh.dddbase.core.lifecycle.forwarding.async

import org.sisioh.dddbase.core.lifecycle.{ResultWithEntity}
import org.sisioh.dddbase.core.lifecycle.async.{AsyncResultWithEntity, AsyncEntityWriter}
import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.concurrent.Future

trait ForwardingAsyncEntityWriter[ID <: Identity[_], T <: Entity[ID]]
  extends AsyncEntityWriter[ID, T] {

  protected val delegateAsyncEntityWriter: AsyncEntityWriter[ID, T]

  protected def createInstance(state: Future[(AsyncEntityWriter[ID, T], Option[T])]): Future[(This, Option[T])]

  def store(entity: T): Future[ResultWithEntity[This, ID, T, Future]] = {
    val state = delegateAsyncEntityWriter.store(entity).map {
      result =>
        (result.result.asInstanceOf[AsyncEntityWriter[ID, T]], Some(result.entity))
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
        (result.asInstanceOf[AsyncEntityWriter[ID, T]], None)
    }
    val instance = createInstance(state)
    instance.map {
      e =>
        e._1
    }
  }

}
