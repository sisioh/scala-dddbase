package org.sisioh.dddbase.core.lifecycle.memory.mutable.async

import org.sisioh.dddbase.core.model.{EntityCloneable, Entity, Identifier}
import org.sisioh.dddbase.core.lifecycle.memory.mutable.sync.GenericSyncRepositoryOnMemory

abstract class AbstractAsyncRepositoryOnMemory [ID <: Identifier[_], E <: Entity[ID] with EntityCloneable[ID, E] with Ordered[E]]
(protected val delegate: GenericSyncRepositoryOnMemory[ID, E] = GenericSyncRepositoryOnMemory[ID, E]())
  extends AsyncRepositoryOnMemory[ID, E] {

  type Delegate = GenericSyncRepositoryOnMemory[ID, E]

}
