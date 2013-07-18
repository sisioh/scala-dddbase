package org.sisioh.dddbase.core.lifecycle.memory.mutable.async

import java.util.UUID
import org.sisioh.dddbase.core.lifecycle.memory.mutable.sync.GenericSyncRepositoryOnMemory
import org.sisioh.dddbase.core.model.Identity
import scala.concurrent.ExecutionContext

class TestAsyncRepositoryOnMemory
(protected val core: GenericSyncRepositoryOnMemory[Identity[UUID], TestEntity] = GenericSyncRepositoryOnMemory[Identity[UUID], TestEntity]())
(implicit val executor: ExecutionContext)
  extends TestAsyncRepository
  with AsyncRepositoryOnMemory[GenericSyncRepositoryOnMemory[Identity[UUID], TestEntity], Identity[UUID], TestEntity] {

  type This = TestAsyncRepositoryOnMemory

}
