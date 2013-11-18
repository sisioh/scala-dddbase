package org.sisioh.dddbase.event.sync

import org.sisioh.dddbase.core.lifecycle.{EntityIOContext, ResultWithEntity, Repository}
import org.sisioh.dddbase.core.model.Identity
import org.sisioh.dddbase.event.{DomainEventStore, DomainEvent}
import scala.util.Try

trait SyncDomainEventStore[+R <: Repository[CTX, ID, T, Try], CTX <: EntityIOContext[Try], ID <: Identity[_], T <: DomainEvent[ID]]
  extends DomainEventStore[R, CTX, ID, T, Try, ResultWithEntity[R#This, CTX, ID, T, Try]]
  with SyncDomainEventSubscriber[T, CTX, ResultWithEntity[R#This, CTX, ID, T, Try]]
