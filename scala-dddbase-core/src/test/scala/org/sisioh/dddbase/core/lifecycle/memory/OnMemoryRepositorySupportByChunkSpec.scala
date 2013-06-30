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
        repository = repository.store(entity).get._1
      }

      val chunk = repository.resolveChunk(0, 5).get

      chunk.index must_== 0
      chunk.entities.size must_== 5
      chunk.entities(0) must_== new EntityImpl(Identity[Int](1))
      chunk.entities(1) must_== new EntityImpl(Identity[Int](2))
      chunk.entities(2) must_== new EntityImpl(Identity[Int](3))
      chunk.entities(3) must_== new EntityImpl(Identity[Int](4))
      chunk.entities(4) must_== new EntityImpl(Identity[Int](5))
    }
  }


}
