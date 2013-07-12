package org.sisioh.dddbase.core.lifecycle

import org.sisioh.dddbase.core.model.{Entity, Identity}

trait EntityReaderByOption[ID <: Identity[_], T <: Entity[ID], M[+A]] {
  this: EntityReader[ID, T, M] =>

  def resolveOption(identity: ID): M[Option[T]]

}
