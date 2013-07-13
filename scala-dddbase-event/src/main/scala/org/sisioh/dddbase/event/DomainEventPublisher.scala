package org.sisioh.dddbase.event

import scala.language.higherKinds

/**
 * ドメインイベントを通知するためのトレイト。
 */
trait DomainEventPublisher
[+DEP <: DomainEventPublisher[_, A, M, R],
A <: DomainEvent[_], M[+B], R] {

  /**
   * [[org.sisioh.dddbase.event.DomainEvent]]を通知する。
   *
   * @param event ドメインイベント
   */
  def publish(event: A): Seq[M[R]]

  /**
   * [[org.sisioh.dddbase.event.DomainEventPublisher]]を登録する。
   *
   * @param subscriber [[org.sisioh.dddbase.event.DomainEventPublisher]]
   */
  def subscribe(subscriber: DomainEventSubscriber[A, M, R]): DEP

  /**
   * [[org.sisioh.dddbase.event.DomainEventPublisher]]を削除する。
   *
   * @param subscriber [[org.sisioh.dddbase.event.DomainEventPublisher]]
   */
  def unsubscribe(subscriber: DomainEventSubscriber[A, M, R]): DEP

}
