package org.sisioh.dddbase.event.lifecycle

import org.specs2.mutable.Specification
import org.sisioh.dddbase.core.lifecycle.memory.mutable.sync.GenericSyncRepositoryOnMemory
import org.sisioh.dddbase.core.model.{EntityCloneable, Entity, Identifier}
import java.util.UUID
import org.sisioh.dddbase.event.sync.SyncDomainEventSubscriber
import scala.util.{Success, Try}
import org.sisioh.dddbase.core.lifecycle.EntityIOContext
import org.sisioh.dddbase.core.lifecycle.sync.SyncEntityIOContext

class SyncRepositoryEventSupportSpec extends Specification {

  class EntityImpl(val identifier: Identifier[UUID]) extends Entity[Identifier[UUID]]
    with EntityCloneable[Identifier[UUID], EntityImpl]
    with Ordered[EntityImpl] {
    def compare(that: EntityImpl): Int = {
      identifier.value.compareTo(that.identifier.value)
    }
  }

  class TestRepository extends GenericSyncRepositoryOnMemory[Identifier[UUID], EntityImpl]
  with SyncRepositoryEventSupport[Identifier[UUID], EntityImpl] {
    protected def createEntityIOEvent(entity: EntityImpl, eventType: EventType.Value):
    EntityIOEvent[Identifier[UUID], EntityImpl] = new EntityIOEvent[Identifier[UUID], EntityImpl](Identifier(UUID.randomUUID()), entity, eventType)
  }

  implicit val ctx = SyncEntityIOContext

  "event subscriber" should {
    "receive entity io event" in {
      var result: Boolean = false
      var resultEntity: EntityImpl = null
      var resultEventType: EventType.Value = null
      val repos = new TestRepository()
      val entity = new EntityImpl(Identifier(UUID.randomUUID()))
      repos.subscribe(new SyncDomainEventSubscriber[EntityIOEvent[Identifier[UUID], EntityImpl], Unit] {
        def handleEvent(event: EntityIOEvent[Identifier[UUID], EntityImpl])(implicit ctx: EntityIOContext[Try]): Try[Unit] = {
          result = true
          resultEntity = event.entity
          resultEventType = event.eventType
          Success(())
        }
      })
      repos.store(entity)
      result must beTrue
      resultEntity must_== entity
      resultEventType must_== EventType.Store
    }
  }

}
