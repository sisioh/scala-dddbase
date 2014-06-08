package org.sisioh.dddbase.event.mutable.async

import java.util.UUID
import org.sisioh.dddbase.lifecycle.forwarding.async.wrapped.AsyncWrappedSyncEntityIOContext
import org.sisioh.dddbase.lifecycle.memory.mutable.async.GenericAsyncRepositoryOnMemory
import org.sisioh.dddbase.core.model.{EntityCloneable, Identifier}
import org.sisioh.dddbase.event.DomainEvent
import org.specs2.mutable.Specification
import scala.concurrent.{Future, Await}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

class GenericAsyncDomainEventStoreSpec extends Specification {

  class TestDomainEvent(val identifier: Identifier[UUID])
    extends DomainEvent[Identifier[UUID]]
    with EntityCloneable[Identifier[UUID], TestDomainEvent]
    with Ordered[TestDomainEvent] {
    def compare(that: TestDomainEvent): Int = {
      identifier.value compareTo that.identifier.value
    }
  }

  implicit val ctx = AsyncWrappedSyncEntityIOContext()

  "domain event store" should {
    "get saved event" in {
      type ID = Identifier[UUID]
      type E = TestDomainEvent
      type REPOS = GenericAsyncRepositoryOnMemory[ID, E]

      val repos = new REPOS
      val target = new GenericAsyncDomainEventStore[REPOS, ID, E](repos)
      val publisher = GenericAsyncDomainEventPublisher[E]()
      val event = new E(Identifier(UUID.randomUUID()))
      val futures = publisher.subscribe(target).publish(event)

      Await.result(Future.sequence(futures), Duration.Inf)
      success
    }
  }
}
