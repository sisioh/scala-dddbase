package org.sisioh.dddbase.event

import org.sisioh.dddbase.core.lifecycle.EntityIOContext
import scala.language.higherKinds

/**
 * ドメインイベントを通知するためのトレイト。
 *
 * @tparam A ドメインイベントの型
 * @tparam M モナドの型
 * @tparam R モナドの値の型
 */
trait DomainEventPublisher
[A <: DomainEvent[_], M[+B], R] {

  /**
   * 派生型
   */
  type This <: DomainEventPublisher[A, M, R]

  type DES <: DomainEventSubscriber[A, M, R]

  /**
   * [[org.sisioh.dddbase.event.DomainEvent]]を通知する。
   *
   * @param event ドメインイベント
   */
  def publish(event: A)(implicit ctx: EntityIOContext[M]): Seq[M[R]]

  /**
   * [[org.sisioh.dddbase.event.DomainEventPublisher]]を登録する。
   *
   * @param subscriber [[org.sisioh.dddbase.event.DomainEventPublisher]]
   */
  def subscribe(subscriber: DES): This

  /**
   * [[org.sisioh.dddbase.event.DomainEventPublisher]]を削除する。
   *
   * @param subscriber [[org.sisioh.dddbase.event.DomainEventPublisher]]
   */
  def unsubscribe(subscriber: DES): This

}
