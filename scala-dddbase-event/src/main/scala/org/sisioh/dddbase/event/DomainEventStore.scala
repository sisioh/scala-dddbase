package org.sisioh.dddbase.event

import scala.language.higherKinds
import org.sisioh.dddbase.core.lifecycle.{RepositoryWithEntity, Repository}
import org.sisioh.dddbase.core.model.Identity

/**
 * イベントを永続化するための[[org.sisioh.dddbase.event.DomainEventSubscriber]]。
 *
 * @tparam R リポジトリの型
 * @tparam ID エンティティの識別子の型
 * @tparam T エンティティの型
 * @tparam M モナドの型
 * @tparam MR モナドの値の型
 */
trait DomainEventStore[+R <: Repository[ID, T, M], ID <: Identity[_], T <: DomainEvent[ID], M[+A], +MR]
  extends DomainEventSubscriber[T, M, MR]

