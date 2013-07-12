package org.sisioh.dddbase.core.lifecycle.memory.mutable

import org.sisioh.dddbase.core.model.{EntityCloneable, Entity, Identity}
import java.util.UUID
import org.sisioh.dddbase.core.lifecycle.AsyncRepository
import scala.concurrent.ExecutionContext

trait TestEntity
  extends Entity[Identity[UUID]]
  with EntityCloneable[Identity[UUID], TestEntity]
  with Ordered[TestEntity] {

}

trait AsyncTestRepository
  extends AsyncRepository[AsyncTestRepository, Identity[UUID], TestEntity] {

}

class AsyncTestRepositoryOnMemory
(protected val core: GenericOnMemorySyncRepository[Identity[UUID], TestEntity] = GenericOnMemorySyncRepository[Identity[UUID], TestEntity]())
(implicit _executor: ExecutionContext )
  extends AsyncTestRepository
  with AsyncOnMemoryRepository[AsyncTestRepository, GenericOnMemorySyncRepository[Identity[UUID], TestEntity], Identity[UUID], TestEntity] {

  implicit val executor: ExecutionContext = _executor

}
