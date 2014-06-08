package org.sisioh.dddbase.event.lifecycle

import org.sisioh.dddbase.core.lifecycle.sync.{SyncResultWithEntity, SyncRepository}
import org.sisioh.dddbase.core.model.{Entity, Identifier}
import org.sisioh.dddbase.event.mutable.sync.GenericSyncDomainEventPublisher
import org.sisioh.dddbase.event.sync.SyncDomainEventSubscriber
import scala.util.Try

/**
 * 同期型リポジトリにイベント管理機能を追加するためのトレイト。
 *
 * @tparam ID エンティティの識別子の型
 * @tparam E エンティティの型
 */
trait SyncRepositoryEventSupport[ID <: Identifier[_], E <: Entity[ID]]
  extends SyncRepository[ID, E] {

  private val eventPublisher = GenericSyncDomainEventPublisher[EntityIOEvent[ID, E]]()

  /**
   * [[org.sisioh.dddbase.event.sync.SyncDomainEventSubscriber]]を登録する。
   *
   * @param subscriber [[org.sisioh.dddbase.event.sync.SyncDomainEventSubscriber]]
   */
  def subscribe(subscriber: SyncDomainEventSubscriber[EntityIOEvent[ID, E], Unit]): This = {
    eventPublisher.subscribe(subscriber)
    this.asInstanceOf[This]
  }

  /**
   * [[org.sisioh.dddbase.event.sync.SyncDomainEventPublisher]]を削除する。
   *
   * @param subscriber [[org.sisioh.dddbase.event.sync.SyncDomainEventPublisher]]
   */
  def unsubscribe(subscriber: SyncDomainEventSubscriber[EntityIOEvent[ID, E], Unit]): This = {
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

  abstract override def store(entity: E)(implicit ctx: Ctx): Try[Result] = {
    val result = super.store(entity).map {
      resultWithEntity =>
        val event = createEntityIOEvent(resultWithEntity.entity, EventType.Store)
        eventPublisher.publish(event)
    }
    result.asInstanceOf[Try[SyncResultWithEntity[This, ID, E]]]
  }

  abstract override def deleteBy(identifier: ID)(implicit ctx: Ctx): Try[Result] = {
    val result = super.deleteBy(identifier).map {
      resultWithEntity =>
        val event = createEntityIOEvent(resultWithEntity.entity, EventType.Delete)
        eventPublisher.publish(event)
    }
    result.asInstanceOf[Try[SyncResultWithEntity[This, ID, E]]]
  }

}
