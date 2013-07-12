package org.sisioh.dddbase.core.lifecycle

import org.sisioh.dddbase.core.model.{Entity, Identity}

trait EntityWriter[+R <: EntityWriter[_, ID, T, M], ID <: Identity[_], T <: Entity[ID], M[+A]]
  extends EntityIO {

  def store(entity: T): M[RepositoryWithEntity[R, T]]

  def update(identity: ID, entity: T) = store(entity)

  def delete(identity: ID): M[R]

  def delete(entity: T): M[R] = delete(entity.identity)

}

