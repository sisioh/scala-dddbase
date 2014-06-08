package org.sisioh.dddbase.event.async

import java.util.UUID
import org.sisioh.dddbase.core.lifecycle.ResultWithEntity
import org.sisioh.dddbase.core.model.{EntityCloneable, Identifier}
import org.sisioh.dddbase.event.DomainEvent
import org.specs2.mutable.Specification
import scala.concurrent.duration.Duration
import scala.concurrent.{Future, Await}
import org.sisioh.dddbase.lifecycle.forwarding.async.wrapped.AsyncWrappedSyncEntityIOContext
import org.sisioh.dddbase.lifecycle.memory.async.GenericAsyncRepositoryOnMemory
import scala.concurrent.ExecutionContext.Implicits.global

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
      val publisher = GenericAsyncDomainEventPublisher[E, ResultWithEntity[REPOS, ID, E, Future]]()
      val event = new E(Identifier(UUID.randomUUID()))
      val futures = publisher.subscribe(target).publish(event)

      futures.map {
        future =>
          val result = Await.result(future, Duration.Inf)
          val contains = Await.result(result.result.existBy(event.identifier), Duration.Inf)
          contains must_== true
      }
    }
  }

}
