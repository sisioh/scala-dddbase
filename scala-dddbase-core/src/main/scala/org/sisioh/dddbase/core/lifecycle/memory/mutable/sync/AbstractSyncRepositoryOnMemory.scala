package org.sisioh.dddbase.core.lifecycle.memory.mutable.sync

import org.sisioh.dddbase.core.model.{EntityCloneable, Entity, Identifier}

abstract class AbstractSyncRepositoryOnMemory[ID <: Identifier[_], E <: Entity[ID] with EntityCloneable[ID, E] with Ordered[E]]
  extends SyncRepositoryOnMemorySupport[ID, E]

