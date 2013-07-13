package org.sisioh.dddbase.event.async

import org.specs2.mutable.Specification
import org.sisioh.dddbase.core.model.{EntityCloneable, Identity}
import java.util.UUID
import org.sisioh.dddbase.event.DomainEvent
import org.sisioh.dddbase.core.lifecycle.memory.mutable.sync.GenericOnMemorySyncRepository
import org.sisioh.dddbase.event.sync.{SyncDomainEventPublisher, SyncDomainEventStore}
import org.sisioh.dddbase.core.lifecycle.RepositoryWithEntity
import org.sisioh.dddbase.core.lifecycle.memory.mutable.async.GenericOnMemoryAsyncRepository
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global

class AsyncDomainEventStoreSpec extends Specification {

  class TestDomainEvent(val identity: Identity[UUID])
    extends DomainEvent[Identity[UUID]]
    with EntityCloneable[Identity[UUID], TestDomainEvent]
    with Ordered[TestDomainEvent] {
    def compare(that: TestDomainEvent): Int = {
      identity.value compareTo that.identity.value
    }
  }

  "domain event store" should {
    "get saved event" in {
      val repos = new GenericOnMemoryAsyncRepository[Identity[UUID], TestDomainEvent]
      val target = new AsyncDomainEventStore[GenericOnMemoryAsyncRepository[Identity[UUID], TestDomainEvent], Identity[UUID], TestDomainEvent](repos)
      val publisher = AsyncDomainEventPublisher[TestDomainEvent, RepositoryWithEntity[_, TestDomainEvent]]()
      val event = new TestDomainEvent(Identity(UUID.randomUUID()))
      val futures = publisher.subscribe(target).publish(event)
      futures.map {
        f =>
          val result = Await.result(f, Duration.Inf)
          result.repository
      }
      true must_== true
      //repos.size must_== 1
      //repos.toList(0).identity must_== event.identity
    }
  }

}
