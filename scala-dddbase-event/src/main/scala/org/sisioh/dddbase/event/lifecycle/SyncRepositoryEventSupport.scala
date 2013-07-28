package org.sisioh.dddbase.event.lifecycle

import org.sisioh.dddbase.core.lifecycle.sync.{SyncResultWithEntity, SyncRepository}
import org.sisioh.dddbase.core.model.{Entity, Identity}
import org.sisioh.dddbase.event.sync.SyncDomainEventSubscriber
import org.sisioh.dddbase.event.mutable.sync.GenericSyncDomainEventPublisher
import scala.util.Try

trait SyncRepositoryEventSupport[ID <: Identity[_], E <: Entity[ID]]
  extends SyncRepository[ID, E] {

  private val eventPublisher = GenericSyncDomainEventPublisher[EntityIOEvent[ID, E]]()

  def subscribe(subscriber: SyncDomainEventSubscriber[EntityIOEvent[ID, E], Unit]): This = {
    eventPublisher.subscribe(subscriber)
    this.asInstanceOf[This]
  }

  def unsubscribe(subscriber: SyncDomainEventSubscriber[EntityIOEvent[ID, E], Unit]): This = {
    eventPublisher.unsubscribe(subscriber)
    this.asInstanceOf[This]
  }

  protected def createEntityIOEvent(entity: E, eventType: EventType.Value): EntityIOEvent[ID, E]

  abstract override def store(entity: E): Try[SyncResultWithEntity[This, ID, E]] = {
    val result = super.store(entity).map{
      resultWithEntity =>
        val event = createEntityIOEvent(resultWithEntity.entity, EventType.Store)
        eventPublisher.publish(event)
    }
    result.asInstanceOf[Try[SyncResultWithEntity[This, ID, E]]]
  }

  abstract override def delete(identity: ID): Try[SyncResultWithEntity[This, ID, E]] = {
    val result = super.delete(identity).map{
      resultWithEntity =>
        val event = createEntityIOEvent(resultWithEntity.entity, EventType.Delete)
        eventPublisher.publish(event)
    }
    result.asInstanceOf[Try[SyncResultWithEntity[This, ID, E]]]
  }

}
