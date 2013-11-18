package org.sisioh.dddbase.core.lifecycle.memory.mutable.async

import java.util.UUID
import org.sisioh.dddbase.core.lifecycle.memory.mutable.sync.GenericSyncRepositoryOnMemory
import org.sisioh.dddbase.core.model.Identity
import scala.concurrent.{Future, ExecutionContext}
import org.sisioh.dddbase.core.lifecycle.EntityIOContext
import scala.util.Try

class TestAsyncRepositoryOnMemory
(protected val delegate: GenericSyncRepositoryOnMemory[EntityIOContext[Try], Identity[UUID], TestEntity] = GenericSyncRepositoryOnMemory[EntityIOContext[Try], Identity[UUID], TestEntity]())
  extends TestAsyncRepository
  with AsyncRepositoryOnMemory[EntityIOContext[Future], Identity[UUID], TestEntity] {

  type This = TestAsyncRepositoryOnMemory

  type Delegate = GenericSyncRepositoryOnMemory[EntityIOContext[Try], Identity[UUID], TestEntity]

}
