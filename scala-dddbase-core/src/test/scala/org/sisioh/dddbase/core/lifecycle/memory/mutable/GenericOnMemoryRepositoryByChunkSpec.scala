package org.sisioh.dddbase.core.lifecycle.memory.mutable

import org.sisioh.dddbase.core.model.{Identity, EntityCloneable, Entity}
import org.specs2.mock.Mockito
import org.specs2.mutable._

class GenericOnMemoryRepositoryByChunkSpec extends Specification with Mockito {

  sequential

  class EntityImpl(val identity: Identity[Int])
    extends Entity[Identity[Int]]
    with EntityCloneable[Identity[Int], EntityImpl]
    with Ordered[EntityImpl] {
    def compare(that: EntityImpl): Int = {
      identity.value.compareTo(that.identity.value)
    }
  }

  class TestSyncRepository
    extends GenericOnMemorySyncRepository[Identity[Int], EntityImpl]
    with OnMemorySyncRepositorySupportByChunkSyncSync[TestSyncRepository, Identity[Int], EntityImpl]

  "The repository" should {
    "have stored entities" in {

      val repository = new TestSyncRepository

      for (i <- 1 to 10) {
        val entity = new EntityImpl(Identity[Int](i))
        repository.store(entity).get.repository
      }

      val chunk = repository.resolveChunk(1, 5).get

      chunk.index must_== 1
      chunk.entities.size must_== 5
      chunk.entities(0).identity.value must_== 6
      chunk.entities(1).identity.value must_== 7
      chunk.entities(2).identity.value must_== 8
      chunk.entities(3).identity.value must_== 9
      chunk.entities(4).identity.value must_== 10
    }
  }

}
