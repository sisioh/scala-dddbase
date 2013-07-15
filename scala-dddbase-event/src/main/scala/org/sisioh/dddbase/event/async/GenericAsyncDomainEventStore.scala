package org.sisioh.dddbase.event.async

import org.sisioh.dddbase.core.lifecycle.Repository
import org.sisioh.dddbase.core.model.Identity
import org.sisioh.dddbase.event.{DomainEventStoreSupport, DomainEvent}
import scala.concurrent.Future

class GenericAsyncDomainEventStore[+R <: Repository[R, ID, T, Future], ID <: Identity[_], T <: DomainEvent[ID]]
(protected val eventRepository: R)
  extends DomainEventStoreSupport[R, ID, T, Future] {

}
