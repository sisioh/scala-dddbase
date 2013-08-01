package org.sisioh.dddbase.core.lifecycle.memory.sync

import java.util.UUID
import org.sisioh.dddbase.core.lifecycle.EntityNotFoundException
import org.sisioh.dddbase.core.model.{EmptyIdentity, Identity, EntityCloneable, Entity}
import org.specs2.mock.Mockito
import org.specs2.mutable._

class GenericSyncRepositoryOnMemorySpec extends Specification with Mockito {

  class EntityImpl(val identity: Identity[UUID])
    extends Entity[Identity[UUID]]
    with EntityCloneable[Identity[UUID], EntityImpl]
    with Ordered[EntityImpl] {
    def compare(that: GenericSyncRepositoryOnMemorySpec.this.type#EntityImpl): Int = {
      identity.value.compareTo(that.identity.value)
    }
  }

  val id = Identity(UUID.randomUUID())

  "The repository" should {
    "have stored enitty with empty identity" in {
      val repository = new GenericSyncRepositoryOnMemory[Identity[UUID], EntityImpl]()
      val entity = spy(new EntityImpl(EmptyIdentity))
      val resultWithEntity = repository.store(entity)
      there was atLeastOne(entity).identity
      repository.resolve(id).isFailure must_== true
      resultWithEntity.flatMap(_.result.contains(entity)).getOrElse(false) must_== true
      (resultWithEntity.get.result ne repository) must beTrue
    }
    "have stored entity" in {
      val repository = new GenericSyncRepositoryOnMemory[Identity[UUID], EntityImpl]()
      val entity = spy(new EntityImpl(id))
      val repos = repository.store(entity)
      there was atLeastOne(entity).identity
      repository.resolve(id).isFailure must_== true
      repos.flatMap(_.result.contains(entity)).getOrElse(false) must_== true
    }
    "resolve a entity by using identity" in {
      val repository = new GenericSyncRepositoryOnMemory[Identity[UUID], EntityImpl]()
      val entity = spy(new EntityImpl(id))
      val repos = repository.store(entity)
      there was atLeastOne(entity).identity
      repository.resolve(id).isFailure must_== true
      repos.flatMap(_.result.resolve(id)).get must_== entity
    }
    "resolveOption a entity by using identity" in {
      class TestSyncRepository
        extends SyncRepositoryOnMemorySupport[Identity[UUID], EntityImpl]
        with SyncRepositoryOnMemorySupportByOption[Identity[UUID], EntityImpl] {
        type This = TestSyncRepository
      }

      val repository = new TestSyncRepository
      val entity = spy(new EntityImpl(id))
      val repos = repository.store(entity)
      there was atLeastOne(entity).identity
      repository.resolveOption(id).isFailure must_== false
      val resolveOptionTry = repos.flatMap {
        r =>
          r.result.resolveOption(id)
      }
      resolveOptionTry.get.get must_== entity
    }
    "delete a entity by using identity" in {
      val repository = new GenericSyncRepositoryOnMemory[Identity[UUID], EntityImpl]()
      val entity = spy(new EntityImpl(id))
      val resultWithEntity = repository.store(entity)
      there was atLeastOne(entity).identity
      repository.resolve(id).isFailure must_== true
      val resultWithEntity2 = resultWithEntity.flatMap(_.result.delete(id)).get
      (resultWithEntity2.result ne repository) must beTrue
      resultWithEntity2.entity must_== entity
    }
    "fail to resolve a entity by a non-existent identity" in {
      val repository = new GenericSyncRepositoryOnMemory[Identity[UUID], EntityImpl]()
      repository.resolve(id).isFailure must_== true
      repository.resolve(id).get must throwA[EntityNotFoundException]
    }
    "fail to delete a entity by a non-existent identity" in {
      val repository = new GenericSyncRepositoryOnMemory[Identity[UUID], EntityImpl]()
      repository.delete(id).isFailure must_== true
      repository.delete(id).get must throwA[EntityNotFoundException]
    }
  }

  "The cloned repository" should {
    val repository = new GenericSyncRepositoryOnMemory[Identity[UUID], EntityImpl]()
    "equals the repository before clone" in {
      repository must_== repository.clone
    }
    "have unequal values to the repository before clone" in {
      val cloneRepository = repository.clone
      val r = repository.entities ne cloneRepository.entities
      r must beTrue
    }
  }
}
