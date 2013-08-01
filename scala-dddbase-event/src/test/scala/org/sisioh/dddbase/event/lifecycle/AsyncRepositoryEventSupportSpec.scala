package org.sisioh.dddbase.event.lifecycle

import org.specs2.mutable.Specification
import org.sisioh.dddbase.core.model.{Entity, EntityCloneable, Identity}
import java.util.UUID
import org.sisioh.dddbase.core.lifecycle.memory.mutable.async.GenericAsyncRepositoryOnMemory
import org.sisioh.dddbase.event.async.AsyncDomainEventSubscriber
import scala.concurrent._
import scala.concurrent.duration.Duration
import ExecutionContext.Implicits.global

class AsyncRepositoryEventSupportSpec extends Specification {

  class EntityImpl(val identity: Identity[UUID]) extends Entity[Identity[UUID]]
  with EntityCloneable[Identity[UUID], EntityImpl]
  with Ordered[EntityImpl] {
    def compare(that: EntityImpl): Int = {
      identity.value.compareTo(that.identity.value)
    }
  }

  class TestRepository extends GenericAsyncRepositoryOnMemory[Identity[UUID], EntityImpl]
  with AsyncRepositoryEventSupport[Identity[UUID], EntityImpl] {
    protected def createEntityIOEvent(entity: EntityImpl, eventType: EventType.Value):
    EntityIOEvent[Identity[UUID], EntityImpl] = new EntityIOEvent[Identity[UUID], EntityImpl](Identity(UUID.randomUUID()), entity, eventType)
  }

  "event subscriber" should {
    "receive entity io event" in {
      var result: Boolean = false
      var resultEntity: EntityImpl = null
      var resultEventType: EventType.Value = null
      val repos = new TestRepository()
      val entity = new EntityImpl(Identity(UUID.randomUUID()))
      repos.subscribe(new AsyncDomainEventSubscriber[EntityIOEvent[Identity[UUID], EntityImpl], Unit] {
        def handleEvent(event: EntityIOEvent[Identity[UUID], EntityImpl]): Future[Unit] = future {
          result = true
          resultEntity = event.entity
          resultEventType = event.eventType
          ()
        }
      })
      Await.result(repos.store(entity), Duration.Inf)
      result must beTrue
      resultEntity must_== entity
      resultEventType must_== EventType.Store
    }
  }
}
