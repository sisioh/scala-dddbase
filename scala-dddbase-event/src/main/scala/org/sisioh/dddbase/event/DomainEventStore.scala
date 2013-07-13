package org.sisioh.dddbase.event

import scala.language.higherKinds
import org.sisioh.dddbase.core.lifecycle.{RepositoryWithEntity, Repository}
import org.sisioh.dddbase.core.model.Identity

trait DomainEventStore[+R <: Repository[R, ID, T, M], ID <: Identity[_], T <: DomainEvent[ID], M[+A]]
  extends DomainEventSubscriber[T, M, RepositoryWithEntity[R, T]]

