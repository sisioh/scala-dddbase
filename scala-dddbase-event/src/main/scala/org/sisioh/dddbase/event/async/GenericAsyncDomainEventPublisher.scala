package org.sisioh.dddbase.event.async

import org.sisioh.dddbase.event.{DomainEventPublisherSupport, DomainEventSubscriber, DomainEvent}
import scala.concurrent.Future

/**
 * [[org.sisioh.dddbase.event.DomainEventPublisher]]の非同期実装。
 *
 * @param subscribers [[org.sisioh.dddbase.event.DomainEventSubscriber]]
 * @tparam A [[org.sisioh.dddbase.event.DomainEvent]]の型
 * @tparam R `handleEvent`の戻り値であるモナドの要素型
 */
case class GenericAsyncDomainEventPublisher[A <: DomainEvent[_], R]
(subscribers: Seq[DomainEventSubscriber[A, Future, R]] = Seq.empty[DomainEventSubscriber[A, Future, R]])
  extends DomainEventPublisherSupport[GenericAsyncDomainEventPublisher[A, R], A, Future, R] {

  protected def createInstance(subscribers: Seq[DomainEventSubscriber[A, Future, R]]): GenericAsyncDomainEventPublisher[A, R] =
    GenericAsyncDomainEventPublisher(subscribers)

}
