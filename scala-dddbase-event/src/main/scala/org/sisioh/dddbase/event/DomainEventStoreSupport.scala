package org.sisioh.dddbase.event

import org.sisioh.dddbase.core.lifecycle._
import org.sisioh.dddbase.core.model.Identity

trait DomainEventStoreSupport[+R <: Repository[_, ID, T, M], ID <: Identity[_], T <: DomainEvent[ID], M[+A]]
  extends DomainEventStore[R, ID, T, M] {

  protected val eventRepository: R

  def handleEvent(event: T): M[RepositoryWithEntity[R, T]] =  ???
    //eventRepository.store(event)


}
