package org.sisioh.dddbase.core.lifecycle.sync

import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.util.Try
import org.sisioh.dddbase.core.lifecycle.ResultWithEntities

trait SyncResultWithEntities[+R <: SyncEntityWriter[ID, T], ID <: Identity[_], T <: Entity[ID]]
  extends ResultWithEntities[R, ID, T, Try]

object SyncResultWithEntities {

  def apply[R <: SyncEntityWriter[ID, T], ID <: Identity[_], T <: Entity[ID]]
  (result: R, entities: Seq[T]):
  SyncResultWithEntities[R, ID, T] =
    SyncResultWithEntitiesImpl(result, entities)

}

private[sync]
case class SyncResultWithEntitiesImpl[+R <: SyncEntityWriter[ID, T], ID <: Identity[_], T <: Entity[ID]]
(result: R, entities: Seq[T])
  extends SyncResultWithEntities[R, ID, T]

