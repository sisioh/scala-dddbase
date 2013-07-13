package org.sisioh.dddbase.event

trait DomainEventPublisherSupport
[+DEP <: DomainEventPublisher[_, A, M, R], A <: DomainEvent[_], M[+B], R]
  extends DomainEventPublisher[DEP, A, M, R] {

  protected val subscribers: Seq[DomainEventSubscriber[A, M, R]]

  protected def createInstance(subscribers: Seq[DomainEventSubscriber[A, M, R]]): DEP


  def publish(event: A): Seq[M[R]] = {
    subscribers.map(_.handleEvent(event))
  }

  def subscribe(subscriber: DomainEventSubscriber[A, M, R]): DEP = {
    createInstance(subscribers :+ subscriber)
  }

  def unsubscribe(subscriber: DomainEventSubscriber[A, M, R]): DEP = {
    createInstance(subscribers.filterNot(_ == subscriber))
  }

}
