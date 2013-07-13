package org.sisioh.dddbase.event.sync

import java.util.UUID
import org.sisioh.dddbase.core.lifecycle.RepositoryWithEntity
import org.sisioh.dddbase.core.lifecycle.memory.mutable.sync.GenericOnMemorySyncRepository
import org.sisioh.dddbase.core.model.{EntityCloneable, Identity}
import org.sisioh.dddbase.event.DomainEvent
import org.specs2.mutable.Specification

class SyncDomainEventStoreSpec extends Specification {

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
      val repos = new GenericOnMemorySyncRepository[Identity[UUID], TestDomainEvent]
      val target = new SyncDomainEventStore[GenericOnMemorySyncRepository[Identity[UUID], TestDomainEvent], Identity[UUID], TestDomainEvent](repos)
      val publisher = SyncDomainEventPublisher[TestDomainEvent, RepositoryWithEntity[_, TestDomainEvent]]()
      val event = new TestDomainEvent(Identity(UUID.randomUUID()))
     val results =  publisher.subscribe(target).publish(event)
      results.map{
        r =>
          r.get.repository
      }

      repos.size must_== 1
      repos.toList(0).identity must_== event.identity
    }
  }


}
