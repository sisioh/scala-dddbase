package org.sisioh.dddbase.event.sync

import org.sisioh.dddbase.core.lifecycle.Repository
import org.sisioh.dddbase.core.model.Identity
import org.sisioh.dddbase.event.{DomainEventStoreSupport, DomainEvent}
import scala.util.Try

class GenericSyncDomainEventStore[+R <: Repository[R, ID, T, Try], ID <: Identity[_], T <: DomainEvent[ID]]
(protected val eventRepository: R)
  extends DomainEventStoreSupport[R, ID, T, Try]
