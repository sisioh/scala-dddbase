package org.sisioh.dddbase.event.sync

import org.sisioh.dddbase.event.{DomainEventPublisher, DomainEvent}
import scala.util.Try
import org.sisioh.dddbase.core.lifecycle.EntityIOContext

trait SyncDomainEventPublisher[A <: DomainEvent[_], CTX <: EntityIOContext[Try], R]
  extends DomainEventPublisher[A, CTX, Try, R]
