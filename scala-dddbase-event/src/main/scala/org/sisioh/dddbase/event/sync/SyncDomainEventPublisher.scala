package org.sisioh.dddbase.event.sync

import org.sisioh.dddbase.event.{DomainEventPublisherSupport, DomainEventSubscriber, DomainEvent}
import scala.util._

case class SyncDomainEventPublisher[A <: DomainEvent[_],R]
(subscribers: Seq[DomainEventSubscriber[A, Try, R]] = Seq.empty[DomainEventSubscriber[A, Try, R]])
  extends DomainEventPublisherSupport[SyncDomainEventPublisher[A, R], A, Try, R] {

  protected def createInstance(subscribers: Seq[DomainEventSubscriber[A, Try, R]]): SyncDomainEventPublisher[A, R] =
    SyncDomainEventPublisher(subscribers)

}
