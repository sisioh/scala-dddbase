package org.sisioh.dddbase.event.mutable.sync

import org.sisioh.dddbase.core.lifecycle.{EntityIOContext, Repository}
import org.sisioh.dddbase.core.model.Identity
import org.sisioh.dddbase.event.DomainEvent
import scala.util.Try

/**
 * 汎用的な[[org.sisioh.dddbase.event.DomainEventStore]]の同期実装。
 *
 * @param eventRepository リポジトリ
 * @tparam R リポジトリの型
 * @tparam ID エンティティの識別子の型
 * @tparam T エンティティの型
 */
case class GenericSyncDomainEventStore[+R <: Repository[CTX, ID, T, Try], CTX <: EntityIOContext[Try] ,ID <: Identity[_], T <: DomainEvent[ID]]
(protected val eventRepository: R)
  extends DomainEventStoreSupport[R, CTX, ID, T]

