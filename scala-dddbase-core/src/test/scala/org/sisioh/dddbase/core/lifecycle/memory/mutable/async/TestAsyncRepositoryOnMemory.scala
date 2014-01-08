package org.sisioh.dddbase.core.lifecycle.memory.mutable.async

import java.util.UUID
import org.sisioh.dddbase.core.lifecycle.memory.mutable.sync.GenericSyncRepositoryOnMemory
import org.sisioh.dddbase.core.model.Identifier
import scala.concurrent.ExecutionContext

class TestAsyncRepositoryOnMemory
(protected val delegate: GenericSyncRepositoryOnMemory[Identifier[UUID], TestEntity] = GenericSyncRepositoryOnMemory[Identifier[UUID], TestEntity]())
  extends TestAsyncRepository
  with AsyncRepositoryOnMemory[Identifier[UUID], TestEntity] {

  type This = TestAsyncRepositoryOnMemory

  type Delegate = GenericSyncRepositoryOnMemory[Identifier[UUID], TestEntity]

}
