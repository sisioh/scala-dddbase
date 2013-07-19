package org.sisioh.dddbase.core.lifecycle.async

import org.sisioh.dddbase.core.lifecycle.ResultWithEntity
import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.concurrent.Future


trait AsyncResultWithEntity[+R <: AsyncEntityWriter[ID, T], ID <: Identity[_], T <: Entity[ID]]
  extends ResultWithEntity[R, ID, T, Future]

object AsyncResultWithEntity {

  def apply[R <: AsyncEntityWriter[ID, T], ID <: Identity[_], T <: Entity[ID]](result: R, entity: T):
  AsyncResultWithEntity[R, ID, T] = AsyncResultWithEntityImpl(result, entity)

  def unapply[R <: AsyncEntityWriter[ID, T], ID <: Identity[_], T <: Entity[ID]]
  (resultWithEntity: AsyncResultWithEntity[R, ID, T]): Option[(R, T)] =
    Some(resultWithEntity.result, resultWithEntity.entity)

}

private[async]
case class AsyncResultWithEntityImpl[+R <: AsyncEntityWriter[ID, T], ID <: Identity[_], T <: Entity[ID]]
(result: R, entity: T) extends AsyncResultWithEntity[R, ID, T]
