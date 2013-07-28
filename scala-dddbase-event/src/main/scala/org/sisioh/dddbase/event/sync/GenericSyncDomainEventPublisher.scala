package org.sisioh.dddbase.event.sync

import org.sisioh.dddbase.event.{DomainEventPublisherSupport, DomainEvent}
import scala.util._

/**
 * 汎用的な[[org.sisioh.dddbase.event.DomainEventPublisher]]の同期型実装。
 *
 * @param subscribers [[org.sisioh.dddbase.event.DomainEventSubscriber]]
 * @tparam A [[org.sisioh.dddbase.event.DomainEvent]]の型
 * @tparam R `handleEvent`の戻り値であるモナドの要素型
 */
case class GenericSyncDomainEventPublisher[A <: DomainEvent[_], R]
(subscribers: Seq[SyncDomainEventSubscriber[A, R]] = Seq.empty[SyncDomainEventSubscriber[A, R]])
  extends SyncDomainEventPublisher[A, R]
  with DomainEventPublisherSupport[A, Try, R] {

  type This = GenericSyncDomainEventPublisher[A, R]

  type DES = SyncDomainEventSubscriber[A, R]

  protected def createInstance(subscribers: Seq[DES]): This =
    GenericSyncDomainEventPublisher(subscribers)

}
