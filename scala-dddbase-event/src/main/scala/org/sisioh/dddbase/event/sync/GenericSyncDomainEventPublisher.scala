package org.sisioh.dddbase.event.sync

import org.sisioh.dddbase.event.{DomainEventPublisherSupport, DomainEvent}
import scala.util._
import org.sisioh.dddbase.core.lifecycle.EntityIOContext

/**
 * 汎用的な[[org.sisioh.dddbase.event.DomainEventPublisher]]の同期型実装。
 *
 * @param subscribers [[org.sisioh.dddbase.event.DomainEventSubscriber]]
 * @tparam A [[org.sisioh.dddbase.event.DomainEvent]]の型
 * @tparam R `handleEvent`の戻り値であるモナドの要素型
 */
case class GenericSyncDomainEventPublisher[A <: DomainEvent[_], CTX <: EntityIOContext[Try], R]
(subscribers: Seq[SyncDomainEventSubscriber[A, CTX, R]] = Seq.empty[SyncDomainEventSubscriber[A, CTX, R]])
  extends SyncDomainEventPublisher[A, CTX, R]
  with DomainEventPublisherSupport[A, CTX, Try, R] {

  type This = GenericSyncDomainEventPublisher[A, CTX, R]

  type DES = SyncDomainEventSubscriber[A, CTX, R]

  protected def createInstance(subscribers: Seq[DES]): This =
    GenericSyncDomainEventPublisher(subscribers)

}
