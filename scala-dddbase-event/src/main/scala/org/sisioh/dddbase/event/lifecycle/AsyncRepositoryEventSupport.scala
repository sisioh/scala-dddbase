package org.sisioh.dddbase.event.lifecycle

import org.sisioh.dddbase.core.lifecycle.async.{AsyncResultWithEntity, AsyncRepository}
import org.sisioh.dddbase.core.model.{Entity, Identity}
import org.sisioh.dddbase.event.async.AsyncDomainEventSubscriber
import org.sisioh.dddbase.event.mutable.async.GenericAsyncDomainEventPublisher
import scala.concurrent.Future

/**
 * 非同期型リポジトリにイベント管理機能を追加するためのトレイト。
 *
 * @tparam ID エンティティの識別子の型
 * @tparam E エンティティの型
 */
trait AsyncRepositoryEventSupport[ID <: Identity[_], E <: Entity[ID]]
  extends AsyncRepository[ID, E] {

  private val eventPublisher = GenericAsyncDomainEventPublisher[EntityIOEvent[ID, E]]()

  /**
   * [[org.sisioh.dddbase.event.async.AsyncDomainEventSubscriber]]を登録する。
   *
   * @param subscriber [[org.sisioh.dddbase.event.async.AsyncDomainEventSubscriber]]
   */
  def subscribe(subscriber: AsyncDomainEventSubscriber[EntityIOEvent[ID, E], Unit]): This = {
    eventPublisher.subscribe(subscriber)
    this.asInstanceOf[This]
  }

  /**
   * [[org.sisioh.dddbase.event.async.AsyncDomainEventPublisher]]を削除する。
   *
   * @param subscriber [[org.sisioh.dddbase.event.async.AsyncDomainEventPublisher]]
   */
  def unsubscribe(subscriber: AsyncDomainEventSubscriber[EntityIOEvent[ID, E], Unit]): This = {
    eventPublisher.unsubscribe(subscriber)
    this.asInstanceOf[This]
  }

  /**
   * [[org.sisioh.dddbase.event.lifecycle.EntityIOEvent]]を生成する。
   *
   * @param entity エンティティ
   * @param eventType イベントの種類
   * @return [[org.sisioh.dddbase.event.lifecycle.EntityIOEvent]]
   */
  protected def createEntityIOEvent(entity: E, eventType: EventType.Value): EntityIOEvent[ID, E]

  abstract override def store(entity: E): Future[AsyncResultWithEntity[This, ID, E]] = {
    val result = super.store(entity)
    result onSuccess {
      case resultWithEntity =>
        val event = createEntityIOEvent(resultWithEntity.entity, EventType.Store)
        eventPublisher.publish(event)
    }
    result.asInstanceOf[Future[AsyncResultWithEntity[This, ID, E]]]
  }

  abstract override def delete(identity: ID): Future[AsyncResultWithEntity[This, ID, E]] = {
    val result = super.delete(identity)
    result onSuccess {
      case resultWithEntity =>
        val event = createEntityIOEvent(resultWithEntity.entity, EventType.Delete)
        eventPublisher.publish(event)
    }
    result.asInstanceOf[Future[AsyncResultWithEntity[This, ID, E]]]
  }

}
