package org.sisioh.dddbase.event.mutable

import scala.language.higherKinds
import org.sisioh.dddbase.event._
import scala.collection.mutable.ArrayBuffer
import org.sisioh.dddbase.core.lifecycle.EntityIOContext

/**
 * [[org.sisioh.dddbase.event.DomainEventPublisher]]の骨格実装。
 *
 * @tparam A ドメインイベントの型
 * @tparam M モナドの型
 * @tparam R モナドの値の型
 */
trait DomainEventPublisherSupport
[A <: DomainEvent[_], CTX <: EntityIOContext[M], M[+B], R]
  extends DomainEventPublisher[A, CTX, M, R] {

  /**
   * [[org.sisioh.dddbase.event.DomainEventSubscriber]]の集合
   */
  protected val subscribers: ArrayBuffer[DES]

  def publish(event: A)(implicit ctx: CTX): Seq[M[R]] = {
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
