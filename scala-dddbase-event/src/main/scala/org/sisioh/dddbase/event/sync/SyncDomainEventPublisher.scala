package org.sisioh.dddbase.event.sync

import org.sisioh.dddbase.event.{DomainEventPublisher, DomainEvent}
import scala.util.Try

trait SyncDomainEventPublisher[A <: DomainEvent[_], R]
  extends DomainEventPublisher[A, Try, R]
