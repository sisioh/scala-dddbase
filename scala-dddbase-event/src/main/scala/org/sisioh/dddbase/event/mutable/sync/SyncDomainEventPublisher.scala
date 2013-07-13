package org.sisioh.dddbase.event.mutable.sync

import org.sisioh.dddbase.event.mutable.DomainEventPublisherSupport
import org.sisioh.dddbase.event.{DomainEventSubscriber, DomainEvent}
import scala.collection.mutable.ArrayBuffer
import scala.util.Try

case class SyncDomainEventPublisher[A <: DomainEvent[_], R]()
  extends DomainEventPublisherSupport[SyncDomainEventPublisher[A, R], A, Try, R] {

  protected lazy val subscribers = ArrayBuffer[DomainEventSubscriber[A, Try, R]]()

}
