package org.sisioh.dddbase.event

import org.sisioh.dddbase.core.lifecycle.{EntityIOContext, Repository}
import org.sisioh.dddbase.core.model.Identity
import scala.language.higherKinds

/**
 * イベントを永続化するための[[org.sisioh.dddbase.event.DomainEventSubscriber]]。
 *
 * @tparam R リポジトリの型
 * @tparam ID エンティティの識別子の型
 * @tparam T エンティティの型
 * @tparam M モナドの型
 * @tparam MR モナドの値の型
 */
trait DomainEventStore[+R <: Repository[CTX, ID, T, M], CTX <: EntityIOContext[M], ID <: Identity[_], T <: DomainEvent[ID], M[+A], +MR]
  extends DomainEventSubscriber[T, CTX, M, MR]

