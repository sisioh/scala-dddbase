package org.sisioh.dddbase.event.lifecycle

import org.sisioh.dddbase.core.lifecycle.async.{AsyncResultWithEntity, AsyncRepository}
import org.sisioh.dddbase.core.model.{Entity, Identity}
import org.sisioh.dddbase.event.async.AsyncDomainEventSubscriber
import org.sisioh.dddbase.event.mutable.async.GenericAsyncDomainEventPublisher
import scala.concurrent.Future

trait AsyncRepositoryEventSupport[ID <: Identity[_], E <: Entity[ID]]
  extends AsyncRepository[ID, E] {

  private val eventPublisher = GenericAsyncDomainEventPublisher[EntityIOEvent[ID, E]]()

  def subscribe(subscriber: AsyncDomainEventSubscriber[EntityIOEvent[ID, E], Unit]): This = {
    eventPublisher.subscribe(subscriber)
    this.asInstanceOf[This]
  }

  def unsubscribe(subscriber: AsyncDomainEventSubscriber[EntityIOEvent[ID, E], Unit]): This = {
    eventPublisher.unsubscribe(subscriber)
    this.asInstanceOf[This]
  }

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
