package org.sisioh.dddbase.core.lifecycle

import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.util.Try

trait ForwardingEntityWriter[+R <: EntityWriter[_, ID, T], ID <: Identity[_], T <: Entity[ID]] extends EntityWriter[R, ID, T] {

  protected val delegateEntityWriter: EntityWriter[R, ID, T]

  def store(entity: T): Try[R] = delegateEntityWriter.store(entity)

  def delete(identity: ID): Try[R] = delegateEntityWriter.delete(identity)

}
