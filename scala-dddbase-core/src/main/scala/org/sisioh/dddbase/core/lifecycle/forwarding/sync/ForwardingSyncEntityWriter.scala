package org.sisioh.dddbase.core.lifecycle.forwarding.sync

import org.sisioh.dddbase.core.lifecycle.{ResultWithEntity}
import org.sisioh.dddbase.core.lifecycle.sync.{SyncResultWithEntity, SyncEntityWriter}
import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.util.Try

trait ForwardingSyncEntityWriter[ID <: Identity[_], T <: Entity[ID]]
  extends SyncEntityWriter[ID, T] {

  type This <: ForwardingSyncEntityWriter[ID, T]

  protected val delegateEntityWriter: SyncEntityWriter[ID, T]

  protected def createInstance(state: Try[(SyncEntityWriter[ID, T]#This, Option[T])]): Try[(This, Option[T])]

  def store(entity: T): Try[ResultWithEntity[This, ID, T, Try]] = {
    createInstance(
      delegateEntityWriter.store(entity).map {
        e =>
          (e.result, Some(e.entity))
      }
    ).map(e => SyncResultWithEntity(e._1, e._2.get))
  }

  def delete(identity: ID): Try[This] = {
    createInstance(
      delegateEntityWriter.delete(identity).map {
        e =>
          (e, None)
      }
    ).map(e => e._1)
  }

}
