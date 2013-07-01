package org.sisioh.dddbase.core.lifecycle.memory.mutable

import org.sisioh.dddbase.core.model.{EntityCloneable, Entity, Identity}
import java.util.UUID
import org.sisioh.dddbase.core.lifecycle.AsyncRepository

trait TestEntity
  extends Entity[Identity[UUID]]
  with EntityCloneable[Identity[UUID], TestEntity]
  with Ordered[TestEntity] {

}

trait AsyncTestRepository
  extends AsyncRepository[AsyncTestRepository, Identity[UUID], TestEntity] {

}

class AsyncTestRepositoryOnMemory
(protected val core: GenericOnMemoryRepository[Identity[UUID], TestEntity] = GenericOnMemoryRepository[Identity[UUID], TestEntity]())
  extends AsyncTestRepository
  with AsyncOnMemoryRepository[AsyncTestRepository, GenericOnMemoryRepository[Identity[UUID], TestEntity], Identity[UUID], TestEntity] {

}
