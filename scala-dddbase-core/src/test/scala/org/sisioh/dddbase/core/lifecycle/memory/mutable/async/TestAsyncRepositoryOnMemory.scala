package org.sisioh.dddbase.core.lifecycle.memory.mutable.async

import java.util.UUID
import org.sisioh.dddbase.core.lifecycle.memory.mutable.sync.GenericSyncRepositoryOnMemory
import org.sisioh.dddbase.core.model.Identity
import scala.concurrent.ExecutionContext

class TestAsyncRepositoryOnMemory
(protected val delegate: GenericSyncRepositoryOnMemory[Identity[UUID], TestEntity] = GenericSyncRepositoryOnMemory[Identity[UUID], TestEntity]())
  extends TestAsyncRepository
  with AsyncRepositoryOnMemory[Identity[UUID], TestEntity] {

  type This = TestAsyncRepositoryOnMemory

  type Delegate = GenericSyncRepositoryOnMemory[Identity[UUID], TestEntity]

}
