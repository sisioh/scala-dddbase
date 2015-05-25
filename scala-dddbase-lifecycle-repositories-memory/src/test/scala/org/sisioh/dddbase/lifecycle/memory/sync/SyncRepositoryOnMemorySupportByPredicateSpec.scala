package org.sisioh.dddbase.lifecycle.memory.sync

import org.sisioh.dddbase.core.lifecycle.sync.SyncEntityIOContext
import org.sisioh.dddbase.core.model._
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification

class SyncRepositoryOnMemorySupportByPredicateSpec extends Specification with Mockito {

  class EntityImpl(val identifier: Identifier[Int])
      extends Entity[Identifier[Int]]
      with EntityCloneable[Identifier[Int], EntityImpl]
      with Ordered[EntityImpl] {

    def compare(that: EntityImpl): Int = {
      this.identifier.value.compareTo(that.identifier.value)
    }

  }

  class TestSyncRepository(entities: Map[Identifier[Int], EntityImpl] = Map.empty)
      extends AbstractSyncRepositoryOnMemory[Identifier[Int], EntityImpl](entities)
      with SyncRepositoryOnMemorySupportAsPredicate[Identifier[Int], EntityImpl] {
    type This = TestSyncRepository

    override protected def createInstance(entities: Map[Identifier[Int], EntityImpl]): TestSyncRepository#This =
      new TestSyncRepository(entities)
  }

  implicit val ctx = SyncEntityIOContext

  "The repository" should {
    "have stored entities" in {

      var repository = new TestSyncRepository

      for (i <- 1 to 10) {
        val entity = new EntityImpl(Identifier[Int](i))
        repository = repository.store(entity).get.result
      }

      val chunk = repository.filterBy(
        {
          e => e.identifier.value % 2 == 0
        }, Some(0), Some(5)).get

      chunk.index must_== 0
      chunk.entities.size must_== 5
      chunk.entities(0).identifier.value must_== 2
      chunk.entities(1).identifier.value must_== 4
      chunk.entities(2).identifier.value must_== 6
      chunk.entities(3).identifier.value must_== 8
      chunk.entities(4).identifier.value must_== 10
    }
  }

}
