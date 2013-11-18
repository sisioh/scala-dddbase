package org.sisioh.dddbase.event

import org.sisioh.dddbase.core.lifecycle._
import org.sisioh.dddbase.core.model.Identity
import scala.language.higherKinds

/**
 * [[org.sisioh.dddbase.event.DomainEventStore]]のための骨格実装。
 *
 * @tparam R リポジトリの型
 * @tparam ID エンティティの識別子の型
 * @tparam T エンティティの型
 * @tparam M モナドの型
 */
trait DomainEventStoreSupport[+R <: Repository[CTX, ID, T, M], CTX <: EntityIOContext[M], ID <: Identity[_], T <: DomainEvent[ID], M[+A]]
  extends DomainEventStore[R, CTX, ID, T, M, ResultWithEntity[R#This, CTX, ID, T, M]] {

  protected val eventRepository: R

  def handleEvent(event: T)(implicit ctx: CTX): M[ResultWithEntity[R#This, CTX, ID, T, M]] = {
    eventRepository.store(event).asInstanceOf[M[ResultWithEntity[R#This, CTX, ID, T, M]]]
  }

}
