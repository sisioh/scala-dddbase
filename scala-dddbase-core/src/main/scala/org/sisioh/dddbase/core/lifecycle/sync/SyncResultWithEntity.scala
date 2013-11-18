package org.sisioh.dddbase.core.lifecycle.sync

import org.sisioh.dddbase.core.lifecycle.{EntityIOContext, ResultWithEntity}
import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.util.Try

trait SyncResultWithEntity[+R <: SyncEntityWriter[CTX, ID, T], CTX <: EntityIOContext[Try], ID <: Identity[_], T <: Entity[ID]]
  extends ResultWithEntity[R, CTX, ID, T, Try]


object SyncResultWithEntity {

  def apply[R <: SyncEntityWriter[CTX, ID, T], CTX <: EntityIOContext[Try], ID <: Identity[_], T <: Entity[ID]]
  (result: R, entity: T):
  SyncResultWithEntity[R, CTX, ID, T] =
    SyncResultWithEntityImpl(result, entity)

  def unapply[R <: SyncEntityWriter[CTX, ID, T], CTX <: EntityIOContext[Try], ID <: Identity[_], T <: Entity[ID]]
  (resultWithEntity: SyncResultWithEntity[R, CTX,  ID, T]):
  Option[(R, T)] = Some(resultWithEntity.result, resultWithEntity.entity)

}

private[sync]
case class SyncResultWithEntityImpl[+R <: SyncEntityWriter[CTX, ID, T], CTX <: EntityIOContext[Try], ID <: Identity[_], T <: Entity[ID]]
(result: R, entity: T)
  extends SyncResultWithEntity[R, CTX, ID, T]
