package org.sisioh.dddbase.event.async

import org.sisioh.dddbase.event.{DomainEventPublisher, DomainEvent}
import scala.concurrent.Future

trait AsyncDomainEventPublisher[A <: DomainEvent[_], R]
  extends DomainEventPublisher[A, Future, R]
