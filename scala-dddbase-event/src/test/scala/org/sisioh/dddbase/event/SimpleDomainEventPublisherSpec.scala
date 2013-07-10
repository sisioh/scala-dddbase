package org.sisioh.dddbase.event

import org.specs2.mutable.Specification
import org.sisioh.dddbase.core.model.Identity
import java.util.UUID

class SimpleDomainEventPublisherSpec extends Specification {

  class TestDomainEvent(val identity: Identity[UUID])
    extends DomainEvent[Identity[UUID]]

  val publisher = new SimpleDomainEventPublisher[TestDomainEvent]

  "dep" should {
    "publish" in {
      var result: Identity[UUID] = null
      publisher.subscribe(
        new DomainEventSubscriber[TestDomainEvent] {
          def handleEvent(event: TestDomainEvent) {
            result = event.identity
          }
        }
      )
      val id = Identity(UUID.randomUUID())
      publisher.publish(new TestDomainEvent(id))
      id must_== result
    }
  }
}
