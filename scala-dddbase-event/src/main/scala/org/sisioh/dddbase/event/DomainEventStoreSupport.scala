package org.sisioh.dddbase.event

import scala.language.higherKinds
import org.sisioh.dddbase.core.lifecycle._
import org.sisioh.dddbase.core.model.Identity

/**
 * [[org.sisioh.dddbase.event.DomainEventStore]]のための骨格実装。
 *
 * @tparam R リポジトリの型
 * @tparam ID エンティティの識別子の型
 * @tparam T エンティティの型
 * @tparam M モナドの型
 */
trait DomainEventStoreSupport[+R <: Repository[R, ID, T, M], ID <: Identity[_], T <: DomainEvent[ID], M[+A]]
  extends DomainEventStore[R, ID, T, M, RepositoryWithEntity[R, T]] {

  protected val eventRepository: R

  def handleEvent(event: T): M[RepositoryWithEntity[R, T]] =
    eventRepository.store(event)

}
