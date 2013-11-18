package org.sisioh.dddbase.event.mutable.async

import org.specs2.mutable.Specification
import org.sisioh.dddbase.core.model.Identity
import java.util.UUID
import org.sisioh.dddbase.event.{DomainEventSubscriber, DomainEvent}
import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global
import org.sisioh.dddbase.event.async.AsyncDomainEventSubscriber
import org.sisioh.dddbase.core.lifecycle.async.AsyncEntityIOContext
import org.sisioh.dddbase.core.lifecycle.EntityIOContext
import scala.util.Try


class GenericAsyncDomainEventPublisherSpec extends Specification {

  class TestDomainEvent(val identity: Identity[UUID])
    extends DomainEvent[Identity[UUID]]

  val publisher = GenericAsyncDomainEventPublisher[TestDomainEvent, EntityIOContext[Future]]()

  implicit val ctx = AsyncEntityIOContext()

  "dep" should {
    "publish" in {
      var result: Identity[UUID] = null
      publisher.subscribe(
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
