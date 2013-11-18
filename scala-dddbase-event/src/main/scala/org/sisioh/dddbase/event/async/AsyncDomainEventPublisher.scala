package org.sisioh.dddbase.event.async

import org.sisioh.dddbase.event.{DomainEventPublisher, DomainEvent}
import scala.concurrent.Future
import org.sisioh.dddbase.core.lifecycle.EntityIOContext

trait AsyncDomainEventPublisher[A <: DomainEvent[_], CTX <: EntityIOContext[Future], R]
  extends DomainEventPublisher[A, CTX, Future, R]
