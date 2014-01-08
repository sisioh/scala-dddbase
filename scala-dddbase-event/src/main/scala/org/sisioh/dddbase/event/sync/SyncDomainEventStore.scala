package org.sisioh.dddbase.event.sync

import org.sisioh.dddbase.core.lifecycle.{ResultWithEntity, Repository}
import org.sisioh.dddbase.core.model.Identifier
import org.sisioh.dddbase.event.{DomainEventStore, DomainEvent}
import scala.util.Try

trait SyncDomainEventStore[+R <: Repository[ID, T, Try], ID <: Identifier[_], T <: DomainEvent[ID]]
  extends DomainEventStore[R, ID, T, Try, ResultWithEntity[R#This, ID, T, Try]]
  with SyncDomainEventSubscriber[T, ResultWithEntity[R#This, ID, T, Try]]
