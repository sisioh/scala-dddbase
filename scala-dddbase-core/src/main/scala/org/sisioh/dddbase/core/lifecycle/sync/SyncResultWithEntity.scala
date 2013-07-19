package org.sisioh.dddbase.core.lifecycle.sync

import org.sisioh.dddbase.core.lifecycle.ResultWithEntity
import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.util.Try

trait SyncResultWithEntity[+R <: SyncEntityWriter[ID, T], ID <: Identity[_], T <: Entity[ID]]
  extends ResultWithEntity[R, ID, T, Try]


object SyncResultWithEntity {

  def apply[R <: SyncEntityWriter[ID, T], ID <: Identity[_], T <: Entity[ID]]
  (result: R, entity: T):
  SyncResultWithEntity[R, ID, T] =
    SyncResultWithEntityImpl(result, entity)

  def unapply[R <: SyncEntityWriter[ID, T], ID <: Identity[_], T <: Entity[ID]]
  (resultWithEntity: SyncResultWithEntity[R, ID, T]):
  Option[(R, T)] = Some(resultWithEntity.result, resultWithEntity.entity)

}

private[sync]
case class SyncResultWithEntityImpl[+R <: SyncEntityWriter[ID, T], ID <: Identity[_], T <: Entity[ID]]
(result: R, entity: T)
  extends SyncResultWithEntity[R, ID, T]
