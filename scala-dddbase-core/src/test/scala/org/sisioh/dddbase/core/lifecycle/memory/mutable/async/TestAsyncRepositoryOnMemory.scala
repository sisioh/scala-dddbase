package org.sisioh.dddbase.core.lifecycle.memory.mutable.async

import java.util.UUID
import org.sisioh.dddbase.core.lifecycle.async.AsyncRepository
import org.sisioh.dddbase.core.lifecycle.memory.mutable.sync.GenericSyncRepositoryOnMemory
import org.sisioh.dddbase.core.model.{EntityCloneable, Entity, Identity}
import scala.concurrent.ExecutionContext

class TestAsyncRepositoryOnMemory
(protected val core: GenericSyncRepositoryOnMemory[Identity[UUID], TestEntity] = GenericSyncRepositoryOnMemory[Identity[UUID], TestEntity]())
(implicit _executor: ExecutionContext)
  extends TestAsyncRepository
  with AsyncRepositoryOnMemory[TestAsyncRepository, GenericSyncRepositoryOnMemory[Identity[UUID], TestEntity], Identity[UUID], TestEntity] {

  implicit val executor: ExecutionContext = _executor

}
