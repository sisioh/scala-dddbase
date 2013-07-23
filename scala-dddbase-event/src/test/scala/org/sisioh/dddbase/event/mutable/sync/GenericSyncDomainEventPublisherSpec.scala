package org.sisioh.dddbase.event.mutable.sync

import java.util.UUID
import org.sisioh.dddbase.core.model.Identity
import org.sisioh.dddbase.event.{DomainEventSubscriber, DomainEvent}
import org.specs2.mutable.Specification
import scala.util._
import org.sisioh.dddbase.event.sync.SyncDomainEventSubscriber

class GenericSyncDomainEventPublisherSpec extends Specification {

  class TestDomainEvent(val identity: Identity[UUID])
    extends DomainEvent[Identity[UUID]]

  val publisher = GenericSyncDomainEventPublisher[TestDomainEvent]()

  "dep" should {
    "publish" in {
      var result: Identity[UUID] = null
      publisher.subscribe(
        new SyncDomainEventSubscriber[TestDomainEvent, Unit] {
          def handleEvent(event: TestDomainEvent): Try[Unit] = {
            result = event.identity
            Success(())
          }
        }
      )
      val id = Identity(UUID.randomUUID())
      publisher.publish(new TestDomainEvent(id))
      id must_== result
    }
  }
}
