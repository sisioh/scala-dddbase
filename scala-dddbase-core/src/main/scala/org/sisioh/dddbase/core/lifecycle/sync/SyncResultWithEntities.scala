package org.sisioh.dddbase.core.lifecycle.sync

import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.util.Try
import org.sisioh.dddbase.core.lifecycle.{EntityIOContext, ResultWithEntities}

trait SyncResultWithEntities[+R <: SyncEntityWriter[CTX, ID, T], CTX <: EntityIOContext[Try], ID <: Identity[_], T <: Entity[ID]]
  extends ResultWithEntities[R, CTX, ID, T, Try]

object SyncResultWithEntities {

  def apply[R <: SyncEntityWriter[CTX, ID, T], CTX <: EntityIOContext[Try], ID <: Identity[_], T <: Entity[ID]]
  (result: R, entities: Seq[T]):
  SyncResultWithEntities[R, CTX, ID, T] =
    SyncResultWithEntitiesImpl(result, entities)

}

private[sync]
case class SyncResultWithEntitiesImpl[+R <: SyncEntityWriter[CTX, ID, T], CTX <: EntityIOContext[Try], ID <: Identity[_], T <: Entity[ID]]
(result: R, entities: Seq[T])
  extends SyncResultWithEntities[R, CTX, ID, T]

