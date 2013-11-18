package org.sisioh.dddbase.event.sync

import org.sisioh.dddbase.core.lifecycle.{EntityIOContext, Repository}
import org.sisioh.dddbase.core.model.Identity
import org.sisioh.dddbase.event.{DomainEventStoreSupport, DomainEvent}
import scala.util.Try

/**
 * 汎用的な[[org.sisioh.dddbase.event.DomainEventStore]]の同期型実装。
 *
 * @param eventRepository リポジトリ
 * @tparam R リポジトリの型
 * @tparam ID エンティティの識別子の型
 * @tparam T エンティティの型
 */
case class GenericSyncDomainEventStore[+R <: Repository[CTX, ID, T, Try], CTX <: EntityIOContext[Try], ID <: Identity[_], T <: DomainEvent[ID]]
(protected val eventRepository: R)
  extends SyncDomainEventStore[R, CTX, ID, T]
  with DomainEventStoreSupport[R, CTX, ID, T, Try]
