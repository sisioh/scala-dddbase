package org.sisioh.dddbase.event.mutable.async

import org.specs2.mutable.Specification
import org.sisioh.dddbase.core.model.Identifier
import java.util.UUID
import org.sisioh.dddbase.event.{DomainEventSubscriber, DomainEvent}
import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global
import org.sisioh.dddbase.event.async.AsyncDomainEventSubscriber
import org.sisioh.dddbase.core.lifecycle.async.AsyncEntityIOContext
import org.sisioh.dddbase.core.lifecycle.EntityIOContext


class GenericAsyncDomainEventPublisherSpec extends Specification {

  class TestDomainEvent(val identifier: Identifier[UUID])
    extends DomainEvent[Identifier[UUID]]

  val publisher = GenericAsyncDomainEventPublisher[TestDomainEvent]()

  implicit val ctx = AsyncEntityIOContext()

  "dep" should {
    "publish" in {
      var result: Identifier[UUID] = null
      publisher.subscribe(
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
