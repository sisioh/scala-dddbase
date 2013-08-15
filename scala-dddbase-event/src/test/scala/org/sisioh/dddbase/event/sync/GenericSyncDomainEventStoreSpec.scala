package org.sisioh.dddbase.event.sync

import java.util.UUID
import org.sisioh.dddbase.core.lifecycle.ResultWithEntity
import org.sisioh.dddbase.core.lifecycle.memory.mutable.sync.GenericSyncRepositoryOnMemory
import org.sisioh.dddbase.core.model.{EntityCloneable, Identity}
import org.sisioh.dddbase.event.DomainEvent
import org.specs2.mutable.Specification
import scala.util.Try
import org.sisioh.dddbase.core.lifecycle.sync.SyncEntityIOContext

class GenericSyncDomainEventStoreSpec extends Specification {

  class TestDomainEvent(val identity: Identity[UUID])
    extends DomainEvent[Identity[UUID]]
    with EntityCloneable[Identity[UUID], TestDomainEvent]
    with Ordered[TestDomainEvent] {
    def compare(that: TestDomainEvent): Int = {
      identity.value compareTo that.identity.value
    }
  }

  implicit val ctx = SyncEntityIOContext

  "domain event store" should {
    "get saved event" in {
      type ID = Identity[UUID]
      type E = TestDomainEvent
      type REPOS = GenericSyncRepositoryOnMemory[ID, E]

      val repos = new REPOS
      val target = new GenericSyncDomainEventStore[REPOS, ID, E](repos)
      val publisher = GenericSyncDomainEventPublisher[E, ResultWithEntity[REPOS, ID, E, Try]]()
      val event = new E(Identity(UUID.randomUUID()))
      val resultTrys = publisher.subscribe(target).publish(event)

      resultTrys.map{
        resultTry =>
          resultTry.map {
            result =>
              result.result.size must_== 1
              result.result.toList(0).identity must_== event.identity
          }.get
      }
    }
  }


}
