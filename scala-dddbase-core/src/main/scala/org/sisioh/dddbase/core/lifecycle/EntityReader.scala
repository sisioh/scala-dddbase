package org.sisioh.dddbase.core.lifecycle

import org.sisioh.dddbase.core.model.{Entity, Identity}

trait EntityReader[ID <: Identity[_], T <: Entity[ID], M[+A]]
  extends EntityIO {

  def resolve(identity: ID): M[T]

  def apply(identity: ID) = resolve(identity)

  def contains(identity: ID): M[Boolean]

  def contains(entity: T): M[Boolean] = contains(entity.identity)

}

