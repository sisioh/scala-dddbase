package org.sisioh.dddbase.event.sync

import org.sisioh.dddbase.event.{DomainEvent, DomainEventSubscriber}
import scala.util.Try
import org.sisioh.dddbase.core.lifecycle.EntityIOContext

trait SyncDomainEventSubscriber[DE <: DomainEvent[_], CTX <: EntityIOContext[Try], +R]
  extends DomainEventSubscriber[DE, CTX, Try, R]


