package org.sisioh.dddbase.event.sync

import org.sisioh.dddbase.event.{DomainEventPublisherSupport, DomainEventSubscriber, DomainEvent}
import scala.util._

case class GenericSyncDomainEventPublisher[A <: DomainEvent[_],R]
(subscribers: Seq[DomainEventSubscriber[A, Try, R]] = Seq.empty[DomainEventSubscriber[A, Try, R]])
  extends DomainEventPublisherSupport[GenericSyncDomainEventPublisher[A, R], A, Try, R] {

  protected def createInstance(subscribers: Seq[DomainEventSubscriber[A, Try, R]]): GenericSyncDomainEventPublisher[A, R] =
    GenericSyncDomainEventPublisher(subscribers)

}
