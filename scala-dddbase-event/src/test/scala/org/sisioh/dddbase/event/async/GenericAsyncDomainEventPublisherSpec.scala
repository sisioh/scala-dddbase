package org.sisioh.dddbase.event.async

import java.util.UUID
import org.sisioh.dddbase.core.model.Identity
import org.sisioh.dddbase.event.DomainEvent
import org.specs2.mutable.Specification
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import org.sisioh.dddbase.core.lifecycle.async.AsyncEntityIOContext
import org.sisioh.dddbase.core.lifecycle.EntityIOContext

class GenericAsyncDomainEventPublisherSpec extends Specification {

  class TestDomainEvent(val identity: Identity[UUID])
    extends DomainEvent[Identity[UUID]]

  var publisher = GenericAsyncDomainEventPublisher[TestDomainEvent, EntityIOContext[Future], Unit]()

  implicit val ctx = AsyncEntityIOContext()

  "dep" should {
    "publish" in {
      var result: Identity[UUID] = null
      publisher = publisher.subscribe(
        new AsyncDomainEventSubscriber[TestDomainEvent, EntityIOContext[Future], Unit] {
          def handleEvent(event: TestDomainEvent)(implicit ctx: EntityIOContext[Future]): Future[Unit] = {
            result = event.identity
            Future(())
          }
        }
      )
      val id = Identity(UUID.randomUUID())
      publisher.publish(new TestDomainEvent(id)).map {
        r =>
          Await.ready(r, Duration.Inf)
          id must_== result
      }
    }
  }

}
