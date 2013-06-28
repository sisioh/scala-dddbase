package org.sisioh.dddbase.core.lifecycle.forwarding

import org.sisioh.dddbase.core.lifecycle.EntityWriter
import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.util.Try

trait ForwardingEntityWriter[+R <: EntityWriter[_, ID, T], ID <: Identity[_], T <: Entity[ID]] extends EntityWriter[R, ID, T] {

  protected val delegateEntityWriter: EntityWriter[_, ID, T]

  protected def createInstance(state: Try[(EntityWriter[_, ID, T], Option[ID])]): Try[(R, Option[ID])]

  def store(entity: T): Try[(R, ID)] = {
    createInstance(
      delegateEntityWriter.store(entity).map {
        e =>
          (e._1.asInstanceOf[R], Some(e._2))
      }
    ).map(e => (e._1, e._2.get))
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
