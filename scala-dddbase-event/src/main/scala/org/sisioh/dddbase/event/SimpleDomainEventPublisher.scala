package org.sisioh.dddbase.event

import scala.collection.mutable.ArrayBuffer

case class SimpleDomainEventPublisher[A <: DomainEvent[_]]()
  extends DomainEventPublisher[A] {

  private val subscribers = ArrayBuffer[DomainEventSubscriber[A]]()

  def publish(event: A) {
    subscribers.foreach(_.handleEvent(event))
  }

  def subscribe(subscriber: DomainEventSubscriber[A]) {
    subscribers += subscriber
  }

  def unsubscribe(subscriber: DomainEventSubscriber[A]) {
    subscribers -= subscriber
  }

}
