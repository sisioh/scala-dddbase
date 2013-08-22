package org.sisioh.dddbase.core.lifecycle.async

import org.sisioh.dddbase.core.model.{Entity, Identity}
import org.sisioh.dddbase.core.lifecycle.ResultWithEntities
import scala.concurrent.Future

trait AsyncResultWithEntities[+R <: AsyncEntityWriter[ID, T], ID <: Identity[_], T <: Entity[ID]]
  extends ResultWithEntities[R, ID, T, Future]

object AsyncResultWithEntities {

  def apply[R <: AsyncEntityWriter[ID, T], ID <: Identity[_], T <: Entity[ID]]
  (result: R, entities: Seq[T]):
  AsyncResultWithEntities[R, ID, T] =
    AsyncResultWithEntitiesImpl(result, entities)

  def unapply[R <: AsyncEntityWriter[ID, T], ID <: Identity[_], T <: Entity[ID]]
  (target: AsyncResultWithEntities[R, ID, T]): Option[(R, Seq[T])] = Some(target.result, target.entities)


}

case class AsyncResultWithEntitiesImpl[+R <: AsyncEntityWriter[ID, T], ID <: Identity[_], T <: Entity[ID]]
(result: R, entities: Seq[T])
  extends AsyncResultWithEntities[R, ID, T]


