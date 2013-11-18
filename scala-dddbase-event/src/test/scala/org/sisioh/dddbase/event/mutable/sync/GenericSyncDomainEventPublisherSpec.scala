package org.sisioh.dddbase.event.mutable.sync

import java.util.UUID
import org.sisioh.dddbase.core.model.Identity
import org.sisioh.dddbase.event.{DomainEventSubscriber, DomainEvent}
import org.specs2.mutable.Specification
import scala.util._
import org.sisioh.dddbase.event.sync.SyncDomainEventSubscriber
import org.sisioh.dddbase.core.lifecycle.sync.SyncEntityIOContext
import org.sisioh.dddbase.core.lifecycle.EntityIOContext
import scala.concurrent.Future

class GenericSyncDomainEventPublisherSpec extends Specification {

  class TestDomainEvent(val identity: Identity[UUID])
    extends DomainEvent[Identity[UUID]]

  val publisher = GenericSyncDomainEventPublisher[TestDomainEvent, EntityIOContext[Try]]()

  implicit val ctx = SyncEntityIOContext

  "dep" should {
    "publish" in {
      var result: Identity[UUID] = null
      publisher.subscribe(
        new SyncDomainEventSubscriber[TestDomainEvent, EntityIOContext[Try], Unit] {
          def handleEvent(event: TestDomainEvent)(implicit ctx: EntityIOContext[Try]): Try[Unit] = {
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
