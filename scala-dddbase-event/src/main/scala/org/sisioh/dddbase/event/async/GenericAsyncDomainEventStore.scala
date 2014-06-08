package org.sisioh.dddbase.event.async

import org.sisioh.dddbase.core.lifecycle.Repository
import org.sisioh.dddbase.core.model.Identifier
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
case class GenericAsyncDomainEventStore[+R <: Repository[ID, T, Future], ID <: Identifier[_], T <: DomainEvent[ID]]
(protected val eventRepository: R)
  extends AsyncDomainEventStore[R, ID, T]
  with DomainEventStoreSupport[R, ID, T, Future]


