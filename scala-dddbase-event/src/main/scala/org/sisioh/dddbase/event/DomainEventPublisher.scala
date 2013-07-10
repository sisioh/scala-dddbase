package org.sisioh.dddbase.event

/**
 * ドメインイベントを通知するためのトレイト。
 */
trait DomainEventPublisher[A <: DomainEvent[_]] {

  /**
   * [[org.sisioh.dddbase.event.DomainEvent]]を通知する。
   *
   * @param event ドメインイベント
   */
  def publish(event: A): Unit

  /**
   * [[org.sisioh.dddbase.event.DomainEventPublisher]]を登録する。
   *
   * @param subscriber [[org.sisioh.dddbase.event.DomainEventPublisher]]
   */
  def subscribe(subscriber: DomainEventSubscriber[A]): Unit

  /**
   * [[org.sisioh.dddbase.event.DomainEventPublisher]]を削除する。
   *
   * @param subscriber [[org.sisioh.dddbase.event.DomainEventPublisher]]
   */
  def unsubscribe(subscriber: DomainEventSubscriber[A]): Unit

}
