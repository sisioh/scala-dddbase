package org.sisioh.dddbase.event

import org.sisioh.dddbase.core.lifecycle._
import org.sisioh.dddbase.core.model.Identifier
import scala.language.higherKinds

/**
 * [[org.sisioh.dddbase.event.DomainEventStore]]のための骨格実装。
 *
 * @tparam R リポジトリの型
 * @tparam ID エンティティの識別子の型
 * @tparam T エンティティの型
 * @tparam M モナドの型
 */
trait DomainEventStoreSupport[+R <: Repository[ID, T, M], ID <: Identifier[_], T <: DomainEvent[ID], M[+A]]
  extends DomainEventStore[R, ID, T, M, ResultWithEntity[R#This, ID, T, M]] {

  protected val eventRepository: R

  def handleEvent(event: T)(implicit ctx: EntityIOContext[M]): M[ResultWithEntity[R#This, ID, T, M]] = {
    eventRepository.store(event).asInstanceOf[M[ResultWithEntity[R#This, ID, T, M]]]
  }

}
