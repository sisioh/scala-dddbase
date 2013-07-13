package org.sisioh.dddbase.event

import org.sisioh.dddbase.core.lifecycle.{RepositoryWithEntity, Repository}
import org.sisioh.dddbase.core.model.Identity

trait DomainEventStore[+R <: Repository[_, ID, T, M], ID <: Identity[_], T <: DomainEvent[ID], M[+A]]
  extends DomainEventSubscriber[T, M, RepositoryWithEntity[_, T]]

