package org.sisioh.dddbase.core.lifecycle

import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.util.Try

trait ForwardingEntityReader[ID <: Identity[_], T <: Entity[ID]] extends EntityReader[ID, T] {

  protected val delegateEntityReader: EntityReader[ID, T]

  def resolve(identity: ID): Try[T] = delegateEntityReader.resolve(identity)

  def contains(identity: ID): Try[Boolean] = delegateEntityReader.contains(identity)

}
