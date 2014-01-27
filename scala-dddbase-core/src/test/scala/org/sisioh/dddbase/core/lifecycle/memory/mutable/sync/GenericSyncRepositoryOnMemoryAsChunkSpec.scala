package org.sisioh.dddbase.core.lifecycle.memory.mutable.sync

import org.sisioh.dddbase.core.lifecycle.memory.mutable.sync._
import org.sisioh.dddbase.core.model.{Identifier, EntityCloneable, Entity}
import org.specs2.mock.Mockito
import org.specs2.mutable._
import org.sisioh.dddbase.core.lifecycle.sync.SyncEntityIOContext

class GenericSyncRepositoryOnMemoryAsChunkSpec extends Specification with Mockito {

  sequential

  class EntityImpl(val identifier: Identifier[Int])
    extends Entity[Identifier[Int]]
    with EntityCloneable[Identifier[Int], EntityImpl]
    with Ordered[EntityImpl] {
    def compare(that: EntityImpl): Int = {
      identifier.value.compareTo(that.identifier.value)
    }
  }

  class TestSyncRepository
    extends SyncRepositoryOnMemory[Identifier[Int], EntityImpl]
    with SyncRepositoryOnMemorySupportAsChunk[Identifier[Int], EntityImpl] {

    type This = TestSyncRepository

  }

  implicit val ctx = SyncEntityIOContext

  "The repository" should {
    "have stored entities" in {

      val repository = new TestSyncRepository

      for (i <- 1 to 10) {
        val entity = new EntityImpl(Identifier[Int](i))
        repository.store(entity).get.result
      }

      val chunk = repository.resolveAsChunk(1, 5).get

      chunk.index must_== 1
      chunk.entities.size must_== 5
      chunk.entities(0).identifier.value must_== 6
      chunk.entities(1).identifier.value must_== 7
      chunk.entities(2).identifier.value must_== 8
      chunk.entities(3).identifier.value must_== 9
      chunk.entities(4).identifier.value must_== 10
    }
  }

}
