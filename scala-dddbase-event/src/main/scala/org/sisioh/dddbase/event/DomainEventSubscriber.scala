package org.sisioh.dddbase.event

/**
 * イベントの通知をハンドリングするためのトレイト。
 */
trait DomainEventSubscriber[DE <: DomainEvent[_], M[+A], R] {

  /**
   * [[org.sisioh.dddbase.event.DomainEvent]]を処理するためのメソッド。
   *
   * @param event [[org.sisioh.dddbase.event.DomainEvent]]
   */
  def handleEvent(event: DE): M[R]

}
