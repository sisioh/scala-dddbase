package org.sisioh.dddbase.event.async

import org.sisioh.dddbase.event.{DomainEvent, DomainEventSubscriber}
import scala.concurrent.Future
import org.sisioh.dddbase.core.lifecycle.{EntityIOContext, EntityIO}

trait AsyncDomainEventSubscriber[DE <: DomainEvent[_], CTX <: EntityIOContext[Future], +R]
  extends DomainEventSubscriber[DE, CTX, Future, R]


