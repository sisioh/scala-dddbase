package org.sisioh.dddbase.event.lifecycle

import java.util.UUID
import org.sisioh.dddbase.core.lifecycle.EntityIOContext
import org.sisioh.dddbase.core.lifecycle.forwarding.async.wrapped.AsyncWrappedSyncEntityIOContext
import org.sisioh.dddbase.core.lifecycle.memory.mutable.async.GenericAsyncRepositoryOnMemory
import org.sisioh.dddbase.core.model.{Entity, EntityCloneable, Identifier}
import org.sisioh.dddbase.event.async.AsyncDomainEventSubscriber
import org.specs2.mutable.Specification
import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

class AsyncRepositoryEventSupportSpec extends Specification {

  class EntityImpl(val identifier: Identifier[UUID])
    extends Entity[Identifier[UUID]]
    with EntityCloneable[Identifier[UUID], EntityImpl]
    with Ordered[EntityImpl] {

    def compare(that: EntityImpl): Int = {
      identifier.value.compareTo(that.identifier.value)
    }

  }

  class TestRepository
    extends GenericAsyncRepositoryOnMemory[Identifier[UUID], EntityImpl]
    with AsyncRepositoryEventSupport[Identifier[UUID], EntityImpl] {

    protected def createEntityIOEvent(entity: EntityImpl, eventType: EventType.Value):
    EntityIOEvent[Identifier[UUID], EntityImpl] = new EntityIOEvent[Identifier[UUID], EntityImpl](Identifier(UUID.randomUUID()), entity, eventType)

  }

  implicit val ctx = AsyncWrappedSyncEntityIOContext()

  "event subscriber" should {
    "receive entity io event" in {
      var result: Boolean = false
      var resultEntity: EntityImpl = null
      var resultEventType: EventType.Value = null
      val repos = new TestRepository()
      val entity = new EntityImpl(Identifier(UUID.randomUUID()))
      repos.subscribe(new AsyncDomainEventSubscriber[EntityIOEvent[Identifier[UUID], EntityImpl], Unit] {
        def handleEvent(event: EntityIOEvent[Identifier[UUID], EntityImpl])(implicit ctx: EntityIOContext[Future]): Future[Unit] = future {
          result = true
          resultEntity = event.entity
          resultEventType = event.eventType
          println("hello")
          ()
        }
      })
      Await.result(repos.store(entity), Duration.Inf)
      Thread.sleep(100)
      result must beTrue
      resultEntity must_== entity
      resultEventType must_== EventType.Store
    }
  }
}
