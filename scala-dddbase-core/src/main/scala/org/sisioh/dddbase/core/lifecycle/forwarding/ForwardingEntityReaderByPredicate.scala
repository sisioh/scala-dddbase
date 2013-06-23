package org.sisioh.dddbase.core.lifecycle.forwarding

import org.sisioh.dddbase.core.lifecycle.{EntitiesChunk, EntityReader, EntityReaderByPredicate}
import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.util.Try

trait ForwardingEntityReaderByPredicate[ID <: Identity[_], T <: Entity[ID]] extends EntityReaderByPredicate[ID, T] {
  this: EntityReader[ID, T] =>

  protected val delegateEntityReaderByPredicate: EntityReaderByPredicate[ID, T]

  def filterByPredicate(predicate: (T) => Boolean, index: Option[Int], maxEntities: Option[Int]): Try[EntitiesChunk[ID, T]] =
    delegateEntityReaderByPredicate.filterByPredicate(predicate, index, maxEntities)

}
