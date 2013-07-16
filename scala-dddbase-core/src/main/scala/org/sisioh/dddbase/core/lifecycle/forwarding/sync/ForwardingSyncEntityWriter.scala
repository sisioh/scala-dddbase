package org.sisioh.dddbase.core.lifecycle.forwarding.sync

import org.sisioh.dddbase.core.lifecycle.RepositoryWithEntity
import org.sisioh.dddbase.core.lifecycle.sync.SyncEntityWriter
import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.util.Try

trait ForwardingSyncEntityWriter[ID <: Identity[_], T <: Entity[ID]]
  extends SyncEntityWriter[ID, T] {

  type R <: ForwardingSyncEntityWriter[ID, T]

  protected val delegateEntityWriter: SyncEntityWriter[ID, T]

  protected def createInstance(state: Try[(SyncEntityWriter[ID, T]#R, Option[T])]): Try[(R, Option[T])]

  def store(entity: T): Try[RepositoryWithEntity[R, T]] = {
    createInstance(
      delegateEntityWriter.store(entity).map {
        e =>
          (e.repository, Some(e.entity))
      }
    ).map(e => RepositoryWithEntity(e._1, e._2.get))
  }

  def delete(identity: ID): Try[R] = {
    createInstance(
      delegateEntityWriter.delete(identity).map {
        e =>
          (e, None)
      }
    ).map(e => e._1)
  }

}
