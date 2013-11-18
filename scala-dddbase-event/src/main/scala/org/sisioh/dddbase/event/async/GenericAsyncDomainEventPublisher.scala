package org.sisioh.dddbase.event.async

import org.sisioh.dddbase.event.{DomainEventPublisherSupport, DomainEventSubscriber, DomainEvent}
import scala.concurrent.Future
import org.sisioh.dddbase.core.lifecycle.EntityIOContext

/**
 * 汎用的な[[org.sisioh.dddbase.event.DomainEventPublisher]]の非同期型実装。
 *
 * @param subscribers [[org.sisioh.dddbase.event.DomainEventSubscriber]]
 * @tparam A [[org.sisioh.dddbase.event.DomainEvent]]の型
 * @tparam R `handleEvent`の戻り値であるモナドの要素型
 */
case class GenericAsyncDomainEventPublisher[A <: DomainEvent[_], CTX <: EntityIOContext[Future], R]
(subscribers: Seq[AsyncDomainEventSubscriber[A, CTX, R]] = Seq.empty[AsyncDomainEventSubscriber[A, CTX, R]])
  extends AsyncDomainEventPublisher[A, CTX, R]
  with DomainEventPublisherSupport[A, CTX, Future, R] {

  type This = GenericAsyncDomainEventPublisher[A, CTX, R]

  type DES = AsyncDomainEventSubscriber[A, CTX, R]

  protected def createInstance(subscribers: Seq[DES]): This =
    GenericAsyncDomainEventPublisher(subscribers)

}
