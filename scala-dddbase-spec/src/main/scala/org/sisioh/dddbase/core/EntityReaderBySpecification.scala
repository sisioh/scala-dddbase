package org.sisioh.dddbase.core

import scala.language.higherKinds
import org.sisioh.dddbase.core.lifecycle.EntitiesChunk
import org.sisioh.dddbase.core.model._
import org.sisioh.dddbase.spec.Specification

trait EntityReaderBySpecification[ID <: Identity[_], T <: Entity[ID], M[+A]] {

  def filterBySpecification
  (specification: Specification[T], index: Option[Int] = None, maxEntities: Option[Int] = None)
  : M[EntitiesChunk[ID, T]]

}
