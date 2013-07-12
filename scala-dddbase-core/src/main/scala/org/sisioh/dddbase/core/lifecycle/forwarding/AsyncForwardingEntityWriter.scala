package org.sisioh.dddbase.core.lifecycle.forwarding

import org.sisioh.dddbase.core.lifecycle.{RepositoryWithEntity, AsyncEntityWriter}
import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.concurrent.{Future, ExecutionContext}

trait AsyncForwardingEntityWriter[R <: AsyncEntityWriter[_, ID, T], ID <: Identity[_], T <: Entity[ID]]
  extends AsyncEntityWriter[R, ID, T] {

  protected val delegateAsyncEntityWriter: AsyncEntityWriter[_, ID, T]

  protected def createInstance(state: Future[(AsyncEntityWriter[_, ID, T], Option[T])]): Future[(R, Option[T])]

  def store(entity: T): Future[RepositoryWithEntity[R, T]] = {
    val state = delegateAsyncEntityWriter.store(entity).map {
      result =>
        (result.repository.asInstanceOf[AsyncEntityWriter[R, ID, T]], Some(result.entity))
    }
    val instance = createInstance(state)
    instance.map {
      e =>
        RepositoryWithEntity(e._1, e._2.get)
    }
  }

  def delete(identity: ID): Future[R] = {
    val state = delegateAsyncEntityWriter.delete(identity).map {
      result =>
        (result.asInstanceOf[AsyncEntityWriter[R, ID, T]], None)
    }
    val instance = createInstance(state)
    instance.map {
      e =>
        e._1
    }
  }

}
