package org.sisioh.dddbase.core.lifecycle.forwarding

import org.sisioh.dddbase.core.lifecycle.{EntityReader, EntityReaderByOption}
import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.util.Try

trait ForwardingEntityReaderByOption[ID <: Identity[_], T <: Entity[ID]] extends EntityReaderByOption[ID, T] {
  this: EntityReader[ID, T] =>

  protected val delegateEntityReaderByOption: EntityReaderByOption[ID, T]

  def resolveOption(identity: ID): Try[Option[T]] = delegateEntityReaderByOption.resolveOption(identity)


}
