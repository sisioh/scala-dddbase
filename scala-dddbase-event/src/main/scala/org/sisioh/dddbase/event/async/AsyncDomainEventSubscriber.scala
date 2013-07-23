package org.sisioh.dddbase.event.async

import org.sisioh.dddbase.event.{DomainEvent, DomainEventSubscriber}
import scala.concurrent.Future

trait AsyncDomainEventSubscriber[DE <: DomainEvent[_], +R]
  extends DomainEventSubscriber[DE, Future, R]


