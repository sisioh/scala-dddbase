package org.sisioh.dddbase.event.async

import org.sisioh.dddbase.event.{DomainEventPublisherSupport, DomainEventSubscriber, DomainEvent}
import scala.concurrent.Future

case class AsyncDomainEventPublisher[A <: DomainEvent[_], R]
(subscribers: Seq[DomainEventSubscriber[A, Future, R]] = Seq.empty[DomainEventSubscriber[A, Future, R]])
  extends DomainEventPublisherSupport[AsyncDomainEventPublisher[A, R], A, Future, R] {

  protected def createInstance(subscribers: Seq[DomainEventSubscriber[A, Future, R]]): AsyncDomainEventPublisher[A, R] =
    AsyncDomainEventPublisher(subscribers)

}
