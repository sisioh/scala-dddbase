package org.sisioh.dddbase.event.async

import java.util.UUID
import org.sisioh.dddbase.core.lifecycle.RepositoryWithEntity
import org.sisioh.dddbase.core.lifecycle.memory.mutable.async.GenericAsyncRepositoryOnMemory
import org.sisioh.dddbase.core.model.{EntityCloneable, Identity}
import org.sisioh.dddbase.event.DomainEvent
import org.specs2.mutable.Specification
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
      type REPOS = GenericAsyncRepositoryOnMemory[Identity[UUID], TestDomainEvent]
      type ID = Identity[UUID]
      type E = TestDomainEvent

      val repos = new REPOS
      val target = new AsyncDomainEventStore[REPOS, ID, E](repos)
      val publisher = AsyncDomainEventPublisher[E, RepositoryWithEntity[REPOS, E]]()
      val event = new TestDomainEvent(Identity(UUID.randomUUID()))
      val futures = publisher.subscribe(target).publish(event)
      futures.map {
        f =>
          val result = Await.result(f, Duration.Inf)
          val contains = Await.result(result.repository.contains(event.identity), Duration.Inf)
          contains must_== true
      }
    }
  }

}
