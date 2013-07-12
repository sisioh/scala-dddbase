package org.sisioh.dddbase.core.lifecycle.forwarding

import org.sisioh.dddbase.core.lifecycle.{RepositoryWithEntity, SyncEntityWriter}
import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.util.Try

trait ForwardingSyncEntityWriter[+R <: SyncEntityWriter[_, ID, T], ID <: Identity[_], T <: Entity[ID]] extends SyncEntityWriter[R, ID, T] {

  protected val delegateEntityWriter: SyncEntityWriter[_, ID, T]

  protected def createInstance(state: Try[(SyncEntityWriter[_, ID, T], Option[T])]): Try[(R, Option[T])]

  def store(entity: T): Try[RepositoryWithEntity[R, T]] = {
    createInstance(
      delegateEntityWriter.store(entity).map {
        e =>
          (e.repository.asInstanceOf[R], Some(e.entity))
      }
    ).map(e => RepositoryWithEntity(e._1, e._2.get))
  }

  def delete(identity: ID): Try[R] = {
    createInstance(
      delegateEntityWriter.delete(identity).map {
        e =>
          (e.asInstanceOf[R], None)
      }
    ).map(e => e._1)
  }

}
