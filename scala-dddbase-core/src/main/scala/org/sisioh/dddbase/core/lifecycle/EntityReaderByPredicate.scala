package org.sisioh.dddbase.core.lifecycle

import org.sisioh.dddbase.core.model.{Entity, Identity}

trait EntityReaderByPredicate[ID <: Identity[_], T <: Entity[ID], M[_]]  {

  def filterByPredicate(predicate: T => Boolean, index: Option[Int] = None, maxEntities: Option[Int] = None): M[EntitiesChunk[ID, T]]

}
