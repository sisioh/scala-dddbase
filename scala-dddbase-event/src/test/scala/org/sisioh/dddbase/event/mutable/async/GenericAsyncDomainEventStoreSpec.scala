package org.sisioh.dddbase.event.mutable.async

import org.specs2.mutable.Specification
import org.sisioh.dddbase.core.model.{EntityCloneable, Identity}
import java.util.UUID
import org.sisioh.dddbase.event.DomainEvent
import org.sisioh.dddbase.core.lifecycle.memory.mutable.async.GenericAsyncRepositoryOnMemory
import scala.concurrent.{Future, Await}
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global
import org.sisioh.dddbase.core.lifecycle.async.AsyncEntityIOContext
import org.sisioh.dddbase.core.lifecycle.forwarding.async.wrapped.AsyncWrappedSyncEntityIOContext
import org.sisioh.dddbase.core.lifecycle.EntityIOContext

class GenericAsyncDomainEventStoreSpec extends Specification {
  class TestDomainEvent(val identity: Identity[UUID])
    extends DomainEvent[Identity[UUID]]
    with EntityCloneable[Identity[UUID], TestDomainEvent]
    with Ordered[TestDomainEvent] {
    def compare(that: TestDomainEvent): Int = {
      identity.value compareTo that.identity.value
    }
  }

  implicit val ctx = AsyncWrappedSyncEntityIOContext()

  "domain event store" should {
    "get saved event" in {
      type ID = Identity[UUID]
      type E = TestDomainEvent
      type CTX = EntityIOContext[Future]
      type REPOS = GenericAsyncRepositoryOnMemory[CTX, ID, E]

      val repos = new REPOS
      val target = new GenericAsyncDomainEventStore[REPOS, ID, E, CTX](repos)
      val publisher = GenericAsyncDomainEventPublisher[E, CTX]()
      val event = new E(Identity(UUID.randomUUID()))
      val futures = publisher.subscribe(target).publish(event)

      futures.map {
        future =>
          val result = Await.result(future, Duration.Inf)
          result must_== ()
      }
    }
  }
}
