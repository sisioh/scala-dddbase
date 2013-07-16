package org.sisioh.dddbase.event

import scala.language.higherKinds

/**
 * [[org.sisioh.dddbase.event.DomainEventPublisher]]のための骨格実装。
 *
 * @tparam A ドメインイベントの型
 * @tparam M モナドの型
 * @tparam R モナドの要素型
 */
trait DomainEventPublisherSupport
[A <: DomainEvent[_], M[+B], R]
  extends DomainEventPublisher[A, M, R] {

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
