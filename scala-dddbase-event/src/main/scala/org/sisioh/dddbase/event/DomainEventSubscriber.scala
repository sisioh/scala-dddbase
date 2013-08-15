package org.sisioh.dddbase.event

import scala.language.higherKinds
import org.sisioh.dddbase.core.lifecycle.EntityIOContext

/**
 * イベントの通知をハンドリングするためのトレイト。
 */
trait DomainEventSubscriber[DE <: DomainEvent[_], M[+A], +R] {

  /**
   * [[org.sisioh.dddbase.event.DomainEvent]]を処理するためのメソッド。
   *
   * @param event [[org.sisioh.dddbase.event.DomainEvent]]
   */
  def handleEvent(event: DE)(implicit ctx: EntityIOContext[M]): M[R]

}
