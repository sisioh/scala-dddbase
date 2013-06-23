package org.sisioh.dddbase.core.lifecycle.forwarding

import org.sisioh.dddbase.core.lifecycle.EntityWriter
import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.util.Try

trait ForwardingEntityWriter[+R <: EntityWriter[_, ID, T], ID <: Identity[_], T <: Entity[ID]] extends EntityWriter[R, ID, T] {

  protected val delegateEntityWriter: EntityWriter[_, ID, T]

  protected def createInstance(state: Try[EntityWriter[_, ID, T]]): Try[R]

  def store(entity: T): Try[R] = createInstance(delegateEntityWriter.store(entity).map(_.asInstanceOf[R]))

  def delete(identity: ID): Try[R] = createInstance(delegateEntityWriter.delete(identity).map(_.asInstanceOf[R]))

}
