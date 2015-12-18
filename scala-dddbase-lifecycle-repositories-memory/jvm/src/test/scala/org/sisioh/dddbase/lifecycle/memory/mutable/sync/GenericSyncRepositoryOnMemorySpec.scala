package org.sisioh.dddbase.lifecycle.memory.mutable.sync

import java.util.UUID
import org.sisioh.dddbase.core.lifecycle.EntityNotFoundException
import org.sisioh.dddbase.core.model._
import org.specs2.mock.Mockito
import org.specs2.mutable._
import scala.Some
import org.sisioh.dddbase.lifecycle.memory.sync.SyncRepositoryOnMemorySupportAsOption

class GenericSyncRepositoryOnMemorySpec extends Specification with Mockito {

  sequential

  class EntityImpl(val identifier: Identifier[UUID])
      extends Entity[Identifier[UUID]]
      with EntityCloneable[Identifier[UUID], EntityImpl]
      with Ordered[EntityImpl] {
    def compare(that: GenericSyncRepositoryOnMemorySpec.this.type#EntityImpl): Int = {
      identifier.value.compareTo(that.identifier.value)
    }
  }

  val id = Identifier(UUID.randomUUID())

  import GenericSyncRepositoryOnMemory.Implicits.defaultEntityIOContext

  "The repository" should {
    "have stored entity with empty identifier" in {
      val repository = GenericSyncRepositoryOnMemory[Identifier[UUID], EntityImpl]()
      val entity = spy(new EntityImpl(EmptyIdentifier))
      val repos = repository.store(entity)
      there was atLeastOne(entity).identifier
      repository.resolveBy(EmptyIdentifier).get must_== entity
      repos.flatMap(_.result.exist(entity)).getOrElse(false) must_== true
    }
    "have stored entity" in {
      val repository = GenericSyncRepositoryOnMemory[Identifier[UUID], EntityImpl]()
      val entity = spy(new EntityImpl(id))
      val repos = repository.store(entity)
      there was atLeastOne(entity).identifier
      repository.resolveBy(id).get must_== entity
      repos.flatMap(_.result.exist(entity)).getOrElse(false) must_== true
    }
    "resolve a entity by using identifier" in {
      val repository = GenericSyncRepositoryOnMemory[Identifier[UUID], EntityImpl]()
      val entity = spy(new EntityImpl(id))
      val repos = repository.store(entity)
      there was atLeastOne(entity).identifier
      repository.resolveBy(id).get must_== entity
      repos.flatMap(_.result.resolveBy(id)).get must_== entity
    }
    "resolveOption a entity by using identifier" in {
      class TestSyncRepository
          extends AbstractSyncRepositoryOnMemory[Identifier[UUID], EntityImpl]
          with SyncRepositoryOnMemorySupportAsOption[Identifier[UUID], EntityImpl] {
        type This = TestSyncRepository

      }
      val repository = new TestSyncRepository
      val entity = spy(new EntityImpl(id))
      val repos = repository.store(entity)
      there was atLeastOne(entity).identifier
      repository.resolveAsOptionBy(id) must_== Some(entity)
      repos.map(_.result.resolveAsOptionBy(id)).get must_== Some(entity)
    }
    "delete a entity by using identifier" in {
      val repository = GenericSyncRepositoryOnMemory[Identifier[UUID], EntityImpl]()
      val entity = spy(new EntityImpl(id))
      val repos = repository.store(entity)
      there was atLeastOne(entity).identifier
      repository.resolveBy(id).get must_== entity
      val resultWithEntity = repos.flatMap(_.result.deleteBy(id)).get
      resultWithEntity.result must_!= repos
      resultWithEntity.entity must_== entity
    }
    "fail to resolve a entity by a non-existent identifier" in {
      val repository = GenericSyncRepositoryOnMemory[Identifier[UUID], EntityImpl]()
      repository.resolveBy(id).isFailure must_== true
      repository.resolveBy(id).get must throwA[EntityNotFoundException]
    }
    "fail to delete a entity by a non-existent identifier" in {
      val repository = GenericSyncRepositoryOnMemory[Identifier[UUID], EntityImpl]()
      repository.deleteBy(id).isFailure must_== true
      repository.deleteBy(id).get must throwA[EntityNotFoundException]
    }
  }
}
