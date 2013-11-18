package org.sisioh.dddbase.event.async

import org.sisioh.dddbase.core.lifecycle.{EntityIOContext, Repository, ResultWithEntity}
import org.sisioh.dddbase.core.model.Identity
import org.sisioh.dddbase.event.{DomainEventStore, DomainEvent}
import scala.concurrent.Future

trait AsyncDomainEventStore[+R <: Repository[CTX, ID, T, Future], CTX <: EntityIOContext[Future], ID <: Identity[_], T <: DomainEvent[ID]]
  extends DomainEventStore[R, CTX, ID, T, Future, ResultWithEntity[R#This, CTX, ID, T, Future]]
  with AsyncDomainEventSubscriber[T, CTX, ResultWithEntity[R#This, CTX, ID, T, Future]]
