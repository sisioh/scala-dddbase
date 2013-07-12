package org.sisioh.dddbase.core.lifecycle.memory.mutable.async

import java.util.UUID
import org.sisioh.dddbase.core.lifecycle.async.AsyncRepository
import org.sisioh.dddbase.core.lifecycle.memory.mutable.sync.GenericOnMemorySyncRepository
import org.sisioh.dddbase.core.model.{EntityCloneable, Entity, Identity}
import scala.concurrent.ExecutionContext

trait TestEntity
  extends Entity[Identity[UUID]]
  with EntityCloneable[Identity[UUID], TestEntity]
  with Ordered[TestEntity] {

}

trait TestAsyncRepository
  extends AsyncRepository[TestAsyncRepository, Identity[UUID], TestEntity] {

}

class TestAsyncRepositoryOnMemory
(protected val core: GenericOnMemorySyncRepository[Identity[UUID], TestEntity] = GenericOnMemorySyncRepository[Identity[UUID], TestEntity]())
(implicit _executor: ExecutionContext)
  extends TestAsyncRepository
  with OnMemoryAsyncRepository[TestAsyncRepository, GenericOnMemorySyncRepository[Identity[UUID], TestEntity], Identity[UUID], TestEntity] {

  implicit val executor: ExecutionContext = _executor

}
