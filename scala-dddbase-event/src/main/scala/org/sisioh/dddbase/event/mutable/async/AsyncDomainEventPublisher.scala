package org.sisioh.dddbase.event.mutable.async

import org.sisioh.dddbase.event.{DomainEventSubscriber, DomainEvent}
import org.sisioh.dddbase.event.mutable.DomainEventPublisherSupport
import scala.concurrent.Future
import scala.collection.mutable.ArrayBuffer

case class AsyncDomainEventPublisher[A <: DomainEvent[_], R]()
  extends DomainEventPublisherSupport[AsyncDomainEventPublisher[A, R], A, Future, R] {

  protected val subscribers =  ArrayBuffer[DomainEventSubscriber[A, Future, R]]()

}
