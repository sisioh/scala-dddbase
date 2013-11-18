package org.sisioh.dddbase.core.lifecycle.async

import org.sisioh.dddbase.core.lifecycle.{EntityIOContext, ResultWithEntity}
import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.concurrent.Future


trait AsyncResultWithEntity[+R <: AsyncEntityWriter[CTX, ID, E], CTX <: EntityIOContext[Future], ID <: Identity[_], E <: Entity[ID]]
  extends ResultWithEntity[R, CTX, ID, E, Future]

object AsyncResultWithEntity {

  def apply[R <: AsyncEntityWriter[CTX, ID, T], CTX <: EntityIOContext[Future], ID <: Identity[_], T <: Entity[ID]](result: R, entity: T):
  AsyncResultWithEntity[R, CTX, ID, T] = AsyncResultWithEntityImpl(result, entity)

  def unapply[R <: AsyncEntityWriter[CTX, ID, T], CTX <: EntityIOContext[Future], ID <: Identity[_], T <: Entity[ID]]
  (resultWithEntity: AsyncResultWithEntity[R, CTX, ID, T]): Option[(R, T)] =
    Some(resultWithEntity.result, resultWithEntity.entity)

}

private[async]
case class AsyncResultWithEntityImpl[+R <: AsyncEntityWriter[CTX, ID, T], CTX <: EntityIOContext[Future], ID <: Identity[_], T <: Entity[ID]]
(result: R, entity: T) extends AsyncResultWithEntity[R, CTX, ID, T]
