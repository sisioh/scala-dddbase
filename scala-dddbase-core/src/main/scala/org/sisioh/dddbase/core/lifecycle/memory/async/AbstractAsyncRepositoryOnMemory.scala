package org.sisioh.dddbase.core.lifecycle.memory.async

import org.sisioh.dddbase.core.model.{EntityCloneable, Entity, Identifier}
import scala.collection

abstract class AbstractAsyncRepositoryOnMemory
[ID <: Identifier[_], E <: Entity[ID] with EntityCloneable[ID, E] with Ordered[E]](entities: Map[ID, E])
  extends AsyncRepositoryOnMemorySupport[ID, E] {

  override protected def getEntities: collection.Map[ID, E] = entities

}
