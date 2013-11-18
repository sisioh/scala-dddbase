package org.sisioh.dddbase.event.async

import org.sisioh.dddbase.core.lifecycle.{EntityIOContext, ResultWithEntity, Repository}
import org.sisioh.dddbase.core.model.Identity
import org.sisioh.dddbase.event.{DomainEventStoreSupport, DomainEvent}
import scala.concurrent.Future

/**
 * 汎用的な[[org.sisioh.dddbase.event.DomainEventStore]]の非同期型実装。
 *
 * @param eventRepository リポジトリ
 * @tparam R リポジトリの型
 * @tparam ID エンティティの識別子の型
 * @tparam T エンティティの型
 */
case class GenericAsyncDomainEventStore[+R <: Repository[CTX, ID, T, Future], CTX <: EntityIOContext[Future], ID <: Identity[_], T <: DomainEvent[ID]]
(protected val eventRepository: R)
  extends AsyncDomainEventStore[R, CTX, ID, T]
  with DomainEventStoreSupport[R, CTX, ID, T, Future]


