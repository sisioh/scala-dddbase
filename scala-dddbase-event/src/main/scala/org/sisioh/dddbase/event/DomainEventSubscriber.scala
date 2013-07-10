package org.sisioh.dddbase.event


/**
 * イベントの通知をハンドリングするためのトレイト。
 */
trait DomainEventSubscriber[A <: DomainEvent[_]] {

  /**
   * [[org.sisioh.dddbase.event.DomainEvent]]を処理するためのメソッド。
   *
   * @param event [[org.sisioh.dddbase.event.DomainEvent]]
   */
  def handleEvent(event: A): Unit

}
