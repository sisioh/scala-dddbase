package org.sisioh.dddbase.core.lifecycle.async

import org.sisioh.dddbase.core.model.{Entity, Identity}
import org.sisioh.dddbase.core.lifecycle.{EntityIOContext, ResultWithEntities}
import scala.concurrent.Future

trait AsyncResultWithEntities[+R <: AsyncEntityWriter[CTX, ID, T], CTX <: EntityIOContext[Future], ID <: Identity[_], T <: Entity[ID]]
  extends ResultWithEntities[R, CTX, ID, T, Future]

object AsyncResultWithEntities {

  def apply[R <: AsyncEntityWriter[CTX, ID, T], CTX <: EntityIOContext[Future], ID <: Identity[_], T <: Entity[ID]]
  (result: R, entities: Seq[T]):
  AsyncResultWithEntities[R, CTX, ID, T] =
    AsyncResultWithEntitiesImpl[R, CTX, ID, T](result, entities)

  def unapply[R <: AsyncEntityWriter[CTX, ID, T], CTX <: EntityIOContext[Future], ID <: Identity[_], T <: Entity[ID]]
  (target: AsyncResultWithEntities[R, CTX, ID, T]): Option[(R, Seq[T])] = Some(target.result, target.entities)


}

case class AsyncResultWithEntitiesImpl[+R <: AsyncEntityWriter[CTX, ID, T], CTX <: EntityIOContext[Future], ID <: Identity[_], T <: Entity[ID]]
(result: R, entities: Seq[T])
  extends AsyncResultWithEntities[R, CTX, ID, T]


