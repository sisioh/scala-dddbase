package org.sisioh.dddbase.event.async

import org.sisioh.dddbase.core.lifecycle.{Repository, ResultWithEntity}
import org.sisioh.dddbase.core.model.Identifier
import org.sisioh.dddbase.event.{DomainEventStore, DomainEvent}
import scala.concurrent.Future

trait AsyncDomainEventStore[+R <: Repository[ID, T, Future], ID <: Identifier[_], T <: DomainEvent[ID]]
  extends DomainEventStore[R, ID, T, Future, ResultWithEntity[R#This, ID, T, Future]]
  with AsyncDomainEventSubscriber[T, ResultWithEntity[R#This, ID, T, Future]]
