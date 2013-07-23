package org.sisioh.dddbase.event.async

import java.util.UUID
import org.sisioh.dddbase.core.model.Identity
import org.sisioh.dddbase.event.{DomainEventSubscriber, DomainEvent}
import org.specs2.mutable.Specification
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

class GenericAsyncDomainEventPublisherSpec extends Specification {

  class TestDomainEvent(val identity: Identity[UUID])
    extends DomainEvent[Identity[UUID]]

  var publisher = GenericAsyncDomainEventPublisher[TestDomainEvent, Unit]()

  "dep" should {
    "publish" in {
      var result: Identity[UUID] = null
      publisher = publisher.subscribe(
        new AsyncDomainEventSubscriber[TestDomainEvent, Unit] {
          def handleEvent(event: TestDomainEvent): Future[Unit] = {
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
