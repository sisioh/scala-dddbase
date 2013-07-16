package org.sisioh.dddbase.event.lifecycle

import org.sisioh.dddbase.core.model.{Entity, Identity}
import org.sisioh.dddbase.event.DomainEvent

class EntityIOEvent[ID <: Identity[_], E <: Entity[_]]
(val identity: ID, val entity: E, val eventType: EventType.Value)
  extends DomainEvent[ID]
