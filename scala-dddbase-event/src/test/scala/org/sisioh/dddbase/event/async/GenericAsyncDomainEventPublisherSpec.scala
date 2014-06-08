package org.sisioh.dddbase.event.async

import java.util.UUID
import org.sisioh.dddbase.core.lifecycle.EntityIOContext
import org.sisioh.dddbase.core.lifecycle.async.AsyncEntityIOContext
import org.sisioh.dddbase.core.model.Identifier
import org.sisioh.dddbase.event.DomainEvent
import org.specs2.mutable.Specification
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

class GenericAsyncDomainEventPublisherSpec extends Specification {

  class TestDomainEvent(val identifier: Identifier[UUID])
    extends DomainEvent[Identifier[UUID]]

  var publisher = GenericAsyncDomainEventPublisher[TestDomainEvent, Unit]()

  implicit val ctx = AsyncEntityIOContext()

  "dep" should {
    "publish" in {
      var result: Identifier[UUID] = null
      publisher = publisher.subscribe(
        new AsyncDomainEventSubscriber[TestDomainEvent, Unit] {
          def handleEvent(event: TestDomainEvent)(implicit ctx: EntityIOContext[Future]): Future[Unit] = {
            result = event.identifier
            Future(())
          }
        }
      )
      val id = Identifier(UUID.randomUUID())
      publisher.publish(new TestDomainEvent(id)).map {
        r =>
          Await.ready(r, Duration.Inf)
          id must_== result
      }
    }
  }

}
