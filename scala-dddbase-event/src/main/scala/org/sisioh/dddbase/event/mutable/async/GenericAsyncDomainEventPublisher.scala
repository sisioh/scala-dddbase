package org.sisioh.dddbase.event.mutable.async

import org.sisioh.dddbase.event.mutable.DomainEventPublisherSupport
import org.sisioh.dddbase.event.{DomainEventSubscriber, DomainEvent}
import scala.collection.mutable.ArrayBuffer
import scala.concurrent.Future
import org.sisioh.dddbase.event.async.AsyncDomainEventSubscriber
import org.sisioh.dddbase.core.lifecycle.EntityIOContext

/**
 * 汎用的な[[org.sisioh.dddbase.event.DomainEventPublisher]]の非同期型実装。
 *
 * @tparam A [[org.sisioh.dddbase.event.DomainEvent]]の型
 */
case class GenericAsyncDomainEventPublisher[A <: DomainEvent[_], CTX <: EntityIOContext[Future]]()
  extends DomainEventPublisherSupport[A, CTX, Future, Unit] {

  type This = GenericAsyncDomainEventPublisher[A, CTX]

  type DES = AsyncDomainEventSubscriber[A, CTX, Unit]

  protected val subscribers = ArrayBuffer[DES]()

}
