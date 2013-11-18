package org.sisioh.dddbase.event.mutable.async

import org.sisioh.dddbase.core.lifecycle.{EntityIOContext, Repository}
import org.sisioh.dddbase.core.model.Identity
import org.sisioh.dddbase.event.{DomainEventStore, DomainEvent}
import scala.concurrent.{Future, ExecutionContext}
import org.sisioh.dddbase.event.async.AsyncDomainEventSubscriber

/**
 * [[org.sisioh.dddbase.event.DomainEventStore]]のための骨格実装。
 *
 * @tparam R リポジトリの型
 * @tparam ID エンティティの識別子の型
 * @tparam T エンティティの型
 */
trait DomainEventStoreSupport
[+R <: Repository[CTX, ID, T, Future],
CTX <: EntityIOContext[Future],
ID <: Identity[_],
T <: DomainEvent[ID]]
  extends DomainEventStore[R, CTX, ID, T, Future, Unit] with AsyncDomainEventSubscriber[T, CTX, Unit] {

  implicit val executor: ExecutionContext

  protected val eventRepository: R

  def handleEvent(event: T)(implicit ctx: CTX): Future[Unit] =
    eventRepository.store(event).map(_ => ())

}
