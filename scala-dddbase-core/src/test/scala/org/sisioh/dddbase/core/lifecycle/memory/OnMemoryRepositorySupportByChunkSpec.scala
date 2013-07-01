package org.sisioh.dddbase.core.lifecycle.memory

import org.specs2.mutable.Specification
import org.sisioh.dddbase.core.model._
import org.specs2.mock.Mockito

class OnMemoryRepositorySupportByChunkSpec extends Specification with Mockito {

  class EntityImpl(val identity: Identity[Int])
    extends Entity[Identity[Int]]
    with EntityCloneable[Identity[Int], EntityImpl]
    with Ordered[EntityImpl] {

    def compare(that: OnMemoryRepositorySupportByChunkSpec.this.type#EntityImpl): Int = {
      this.identity.value.compareTo(that.identity.value)
    }

  }

  "The repository" should {
    "have stored entities" in {
      class TestRepository
        extends GenericOnMemoryRepository[Identity[Int], EntityImpl]()
        with OnMemoryRepositorySupportByChunk[TestRepository, Identity[Int], EntityImpl]

      var repository = new TestRepository

      for (i <- 1 to 10) {
        val entity = new EntityImpl(Identity[Int](i))
        repository = repository.store(entity).get.repository
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
