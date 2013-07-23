package org.sisioh.dddbase.event.sync

import org.sisioh.dddbase.event.{DomainEvent, DomainEventSubscriber}
import scala.util.Try

trait SyncDomainEventSubscriber[DE <: DomainEvent[_], +R]
  extends DomainEventSubscriber[DE, Try, R]


