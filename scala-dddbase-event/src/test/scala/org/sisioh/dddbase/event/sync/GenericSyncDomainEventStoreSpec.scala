package org.sisioh.dddbase.event.sync

import java.util.UUID
import org.sisioh.dddbase.core.lifecycle.ResultWithEntity
import org.sisioh.dddbase.core.lifecycle.memory.mutable.sync.GenericSyncRepositoryOnMemory
import org.sisioh.dddbase.core.model.{EntityCloneable, Identifier}
import org.sisioh.dddbase.event.DomainEvent
import org.specs2.mutable.Specification
import scala.util.Try
import org.sisioh.dddbase.core.lifecycle.sync.SyncEntityIOContext

class GenericSyncDomainEventStoreSpec extends Specification {

  class TestDomainEvent(val identifier: Identifier[UUID])
    extends DomainEvent[Identifier[UUID]]
    with EntityCloneable[Identifier[UUID], TestDomainEvent]
    with Ordered[TestDomainEvent] {
    def compare(that: TestDomainEvent): Int = {
      identifier.value compareTo that.identifier.value
    }
  }

  implicit val ctx = SyncEntityIOContext

  "domain event store" should {
    "get saved event" in {
      type ID = Identifier[UUID]
      type E = TestDomainEvent
      type REPOS = GenericSyncRepositoryOnMemory[ID, E]

      val repos = new REPOS
      val target = new GenericSyncDomainEventStore[REPOS, ID, E](repos)
      val publisher = GenericSyncDomainEventPublisher[E, ResultWithEntity[REPOS, ID, E, Try]]()
      val event = new E(Identifier(UUID.randomUUID()))
      val resultTrys = publisher.subscribe(target).publish(event)

      resultTrys.map{
        resultTry =>
          resultTry.map {
            result =>
              result.result.size must_== 1
              result.result.toList(0).identifier must_== event.identifier
          }.get
      }
    }
  }


}
