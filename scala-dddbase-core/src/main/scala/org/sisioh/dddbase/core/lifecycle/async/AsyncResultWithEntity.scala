package org.sisioh.dddbase.core.lifecycle.async

import org.sisioh.dddbase.core.lifecycle.ResultWithEntity
import org.sisioh.dddbase.core.model.{Entity, Identifier}
import scala.concurrent.Future


trait AsyncResultWithEntity[+R <: AsyncEntityWriter[ID, E], ID <: Identifier[_], E <: Entity[ID]]
  extends ResultWithEntity[R, ID, E, Future]

object AsyncResultWithEntity {

  def apply[R <: AsyncEntityWriter[ID, T], ID <: Identifier[_], T <: Entity[ID]](result: R, entity: T):
  AsyncResultWithEntity[R, ID, T] = AsyncResultWithEntityImpl(result, entity)

  def unapply[R <: AsyncEntityWriter[ID, T], ID <: Identifier[_], T <: Entity[ID]]
  (resultWithEntity: AsyncResultWithEntity[R, ID, T]): Option[(R, T)] =
    Some(resultWithEntity.result, resultWithEntity.entity)

}

private[async]
case class AsyncResultWithEntityImpl[+R <: AsyncEntityWriter[ID, T], ID <: Identifier[_], T <: Entity[ID]]
(result: R, entity: T) extends AsyncResultWithEntity[R, ID, T]
