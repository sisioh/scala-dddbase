package org.sisioh.dddbase.core.lifecycle.memory.async

import org.sisioh.dddbase.core.model.{EntityCloneable, Entity, Identifier}
import org.sisioh.dddbase.core.lifecycle.memory.sync.GenericSyncRepositoryOnMemory

abstract class AbstractAsyncRepositoryOnMemory[ID <: Identifier[_], E <: Entity[ID] with EntityCloneable[ID, E] with Ordered[E]]
(protected val delegate: GenericSyncRepositoryOnMemory[ID, E] = GenericSyncRepositoryOnMemory[ID, E]())
  extends AsyncRepositoryOnMemorySupport[ID, E]  {

  type Delegate = GenericSyncRepositoryOnMemory[ID, E]

}
