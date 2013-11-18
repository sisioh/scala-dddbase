package org.sisioh.dddbase.event.mutable.sync

import org.sisioh.dddbase.event.DomainEvent
import org.sisioh.dddbase.event.mutable.DomainEventPublisherSupport
import org.sisioh.dddbase.event.sync.SyncDomainEventSubscriber
import scala.collection.mutable.ArrayBuffer
import scala.util.Try
import org.sisioh.dddbase.core.lifecycle.EntityIOContext

/**
 * 汎用的な[[org.sisioh.dddbase.event.DomainEventPublisher]]の同期型実装。
 *
 * @tparam A [[org.sisioh.dddbase.event.DomainEvent]]の型
 */
case class GenericSyncDomainEventPublisher[A <: DomainEvent[_], CTX <: EntityIOContext[Try]]()
  extends DomainEventPublisherSupport[A, CTX, Try, Unit] {

  type This = GenericSyncDomainEventPublisher[A, CTX]

  type DES = SyncDomainEventSubscriber[A, CTX, Unit]

  protected lazy val subscribers = ArrayBuffer[DES]()

}
