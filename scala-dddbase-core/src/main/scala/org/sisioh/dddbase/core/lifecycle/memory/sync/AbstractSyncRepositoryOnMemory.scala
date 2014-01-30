package org.sisioh.dddbase.core.lifecycle.memory.sync

import org.sisioh.dddbase.core.model.{EntityCloneable, Entity, Identifier}
import scala.collection

abstract class AbstractSyncRepositoryOnMemory
[ID <: Identifier[_], E <: Entity[ID] with EntityCloneable[ID, E] with Ordered[E]](entities: Map[ID, E])
  extends SyncRepositoryOnMemorySupport[ID, E]  {
  override protected def getEntities: collection.Map[ID, E] = entities
}
