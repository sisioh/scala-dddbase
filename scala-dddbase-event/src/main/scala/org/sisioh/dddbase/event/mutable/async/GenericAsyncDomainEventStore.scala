package org.sisioh.dddbase.event.mutable.async

import org.sisioh.dddbase.core.lifecycle.Repository
import org.sisioh.dddbase.core.model.Identity
import org.sisioh.dddbase.event.DomainEvent
import scala.concurrent.{ExecutionContext, Future}

/**
 * 汎用的な[[org.sisioh.dddbase.event.DomainEventStore]]の非同期実装。
 *
 * @param eventRepository リポジトリ
 * @param executor `scala.concurrent.ExecutionContext`
 * @tparam R リポジトリの型
 * @tparam ID エンティティの識別子の型
 * @tparam T エンティティの型
 */
case class GenericAsyncDomainEventStore[+R <: Repository[ID, T, Future], ID <: Identity[_], T <: DomainEvent[ID]]
(protected val eventRepository: R)(implicit val executor: ExecutionContext)
  extends DomainEventStoreSupport[R, ID, T]
