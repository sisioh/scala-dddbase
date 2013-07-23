package org.sisioh.dddbase.event.mutable.async

import org.sisioh.dddbase.event.mutable.DomainEventPublisherSupport
import org.sisioh.dddbase.event.{DomainEventSubscriber, DomainEvent}
import scala.collection.mutable.ArrayBuffer
import scala.concurrent.Future

/**
 * 汎用的な[[org.sisioh.dddbase.event.DomainEventPublisher]]の非同期型実装。
 *
 * @tparam A [[org.sisioh.dddbase.event.DomainEvent]]の型
 */
case class GenericAsyncDomainEventPublisher[A <: DomainEvent[_]]()
  extends DomainEventPublisherSupport[A, Future, Unit] {

  type This = GenericAsyncDomainEventPublisher[A]

  protected val subscribers = ArrayBuffer[DomainEventSubscriber[A, Future, Unit]]()

}
