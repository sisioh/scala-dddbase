package org.sisioh.dddbase.event.mutable

import scala.language.higherKinds
import org.sisioh.dddbase.event._
import scala.collection.mutable.ArrayBuffer

trait DomainEventPublisherSupport
[+DEP <: DomainEventPublisher[_, A, M, R],
A <: DomainEvent[_], M[+B], R]
  extends DomainEventPublisher[DEP, A, M, R] {

  protected val subscribers: ArrayBuffer[DomainEventSubscriber[A, M, R]]

  def publish(event: A): Seq[M[R]] = {
    subscribers.map(_.handleEvent(event))
  }

  def subscribe(subscriber: DomainEventSubscriber[A, M, R]): DEP = {
    subscribers += subscriber
    this.asInstanceOf[DEP]
  }

  def unsubscribe(subscriber: DomainEventSubscriber[A, M, R]): DEP = {
    subscribers -= subscriber
    this.asInstanceOf[DEP]
  }

}
