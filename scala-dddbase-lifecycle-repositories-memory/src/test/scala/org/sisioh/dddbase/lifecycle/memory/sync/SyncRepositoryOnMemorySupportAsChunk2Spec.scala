package org.sisioh.dddbase.lifecycle.memory.sync

import org.sisioh.dddbase.core.lifecycle.sync.SyncEntityIOContext
import org.sisioh.dddbase.core.model._
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification

class SyncRepositoryOnMemorySupportAsChunk2Spec extends Specification with Mockito {

  case class IntIdentifier(value: Int)
    extends AbstractOrderedIdentifier[Int, IntIdentifier]

  class EntityImpl(val identifier: IntIdentifier)
    extends Entity[IntIdentifier]
    with EntityCloneable[IntIdentifier, EntityImpl]
    with EntityOrdered[Int, IntIdentifier, EntityImpl]

  class TestSyncRepository(entities: Map[IntIdentifier, EntityImpl] = Map.empty)
      extends AbstractSyncRepositoryOnMemory[IntIdentifier, EntityImpl](entities)
      with SyncRepositoryOnMemorySupportAsChunk[IntIdentifier, EntityImpl] {
    type This = TestSyncRepository

    override protected def createInstance(entities: Map[IntIdentifier, EntityImpl]): This =
      new TestSyncRepository(entities)
  }

  implicit val ctx = SyncEntityIOContext

  "The repository" should {
    "have stored entities" in {

      var repository = new TestSyncRepository()

      for (i <- 1 to 10) {
        val entity = new EntityImpl(IntIdentifier(i))
        repository = repository.store(entity).get.result
      }

      val chunk = repository.resolveAsChunk(0, 5).get

      chunk.index must_== 0
      chunk.entities.size must_== 5
      chunk.entities(0) must_== new EntityImpl(IntIdentifier(1))
      chunk.entities(1) must_== new EntityImpl(IntIdentifier(2))
      chunk.entities(2) must_== new EntityImpl(IntIdentifier(3))
      chunk.entities(3) must_== new EntityImpl(IntIdentifier(4))
      chunk.entities(4) must_== new EntityImpl(IntIdentifier(5))
    }
  }

}
