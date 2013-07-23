package org.sisioh.dddbase.event.mutable

import scala.language.higherKinds
import org.sisioh.dddbase.event._
import scala.collection.mutable.ArrayBuffer

/**
 * [[org.sisioh.dddbase.event.DomainEventPublisher]]の骨格実装。
 *
 * @tparam A ドメインイベントの型
 * @tparam M モナドの型
 * @tparam R モナドの値の型
 */
trait DomainEventPublisherSupport
[A <: DomainEvent[_], M[+B], R]
  extends DomainEventPublisher[A, M, R] {

  /**
   * [[org.sisioh.dddbase.event.DomainEventSubscriber]]の集合
   */
  protected val subscribers: ArrayBuffer[DES]

  def publish(event: A): Seq[M[R]] = {
    subscribers.map(_.handleEvent(event))
  }

  def subscribe(subscriber: DES): This = {
    subscribers += subscriber
    this.asInstanceOf[This]
  }

  def unsubscribe(subscriber: DES): This = {
    subscribers -= subscriber
    this.asInstanceOf[This]
  }

}
