package org.sisioh.dddbase.event.mutable.sync

import org.sisioh.dddbase.core.lifecycle.{EntityIOContext, Repository}
import org.sisioh.dddbase.core.model.Identity
import org.sisioh.dddbase.event.{DomainEventStore, DomainEvent}
import scala.util.Try
import org.sisioh.dddbase.event.sync.SyncDomainEventSubscriber

/**
 * [[org.sisioh.dddbase.event.DomainEventStore]]のための骨格実装。
 *
 * @tparam R リポジトリの型
 * @tparam ID エンティティの識別子の型
 * @tparam T エンティティの型
 */
trait DomainEventStoreSupport
[+R <: Repository[CTX, ID, T, Try],
CTX <: EntityIOContext[Try],
ID <: Identity[_],
T <: DomainEvent[ID]]
  extends DomainEventStore[R, CTX, ID, T, Try, Unit]
  with SyncDomainEventSubscriber[T, CTX, Unit] {

  protected val eventRepository: R

  def handleEvent(event: T)(implicit ctx: CTX): Try[Unit] =
    eventRepository.store(event).map(_ => ())

}
