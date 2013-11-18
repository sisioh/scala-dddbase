package org.sisioh.dddbase.core.lifecycle.memory.sync

import org.sisioh.dddbase.core.model._
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.sisioh.dddbase.core.lifecycle.sync.SyncEntityIOContext
import org.sisioh.dddbase.core.lifecycle.EntityIOContext
import scala.util.Try

class SyncRepositoryOnMemorySupportByPredicateSpec extends Specification with Mockito {

  class EntityImpl(val identity: Identity[Int])
    extends Entity[Identity[Int]]
    with EntityCloneable[Identity[Int], EntityImpl]
    with Ordered[EntityImpl] {

    def compare(that: EntityImpl): Int = {
      this.identity.value.compareTo(that.identity.value)
    }

  }

  class TestSyncRepository
    extends SyncRepositoryOnMemorySupport[EntityIOContext[Try], Identity[Int], EntityImpl]()
    with SyncRepositoryOnMemorySupportByPredicate[EntityIOContext[Try], Identity[Int], EntityImpl] {
    override type This = TestSyncRepository
  }

  implicit val ctx = SyncEntityIOContext

  "The repository" should {
    "have stored entities" in {

      var repository = new TestSyncRepository

      for (i <- 1 to 10) {
        val entity = new EntityImpl(Identity[Int](i))
        repository = repository.store(entity).get.result
      }

      val chunk = repository.filterByPredicate(
      {
        e => e.identity.value % 2 == 0
      }, Some(0), Some(5)).get

      chunk.index must_== 0
      chunk.entities.size must_== 5
      chunk.entities(0).identity.value must_== 2
      chunk.entities(1).identity.value must_== 4
      chunk.entities(2).identity.value must_== 6
      chunk.entities(3).identity.value must_== 8
      chunk.entities(4).identity.value must_== 10
    }
  }


}
