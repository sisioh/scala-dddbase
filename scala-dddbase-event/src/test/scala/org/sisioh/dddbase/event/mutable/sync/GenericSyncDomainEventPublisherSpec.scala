package org.sisioh.dddbase.event.mutable.sync

import java.util.UUID
import org.sisioh.dddbase.core.model.Identifier
import org.sisioh.dddbase.event.{DomainEventSubscriber, DomainEvent}
import org.specs2.mutable.Specification
import scala.util._
import org.sisioh.dddbase.event.sync.SyncDomainEventSubscriber
import org.sisioh.dddbase.core.lifecycle.sync.SyncEntityIOContext
import org.sisioh.dddbase.core.lifecycle.EntityIOContext

class GenericSyncDomainEventPublisherSpec extends Specification {

  class TestDomainEvent(val identifier: Identifier[UUID])
    extends DomainEvent[Identifier[UUID]]

  val publisher = GenericSyncDomainEventPublisher[TestDomainEvent]()

  implicit val ctx = SyncEntityIOContext

  "dep" should {
    "publish" in {
      var result: Identifier[UUID] = null
      publisher.subscribe(
        new SyncDomainEventSubscriber[TestDomainEvent, Unit] {
          def handleEvent(event: TestDomainEvent)(implicit ctx: EntityIOContext[Try]): Try[Unit] = {
            result = event.identifier
            Success(())
          }
        }
      )
      val id = Identifier(UUID.randomUUID())
      publisher.publish(new TestDomainEvent(id))
      id must_== result
    }
  }
}
