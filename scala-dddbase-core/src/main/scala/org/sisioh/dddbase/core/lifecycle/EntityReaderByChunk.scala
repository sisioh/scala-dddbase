package org.sisioh.dddbase.core.lifecycle

import org.sisioh.dddbase.core.model.{Entity, Identity}

trait EntityReaderByChunk[ID <: Identity[_], T <: Entity[ID], M[_]]  {

  def resolveChunk(index: Int, maxEntities: Int): M[EntitiesChunk[ID, T]]

}
