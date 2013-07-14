package org.sisioh.dddbase.core.lifecycle.memory.mutable.sync

import java.util.UUID
import org.sisioh.dddbase.core.lifecycle.EntityNotFoundException
import org.sisioh.dddbase.core.lifecycle.EntityNotFoundException
import org.sisioh.dddbase.core.lifecycle.memory.mutable.sync._
import org.sisioh.dddbase.core.model.{EmptyIdentity, Identity, EntityCloneable, Entity}
import org.specs2.mock.Mockito
import org.specs2.mutable._
import scala.Some
import scala.util.Failure

class GenericSyncRepositoryOnMemorySpec extends Specification with Mockito {

  sequential

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
    "have stored entity with empty identity" in {
      val repository = new GenericSyncRepositoryOnMemory[Identity[UUID], EntityImpl]()
      val entity = spy(new EntityImpl(EmptyIdentity))
      val repos = repository.store(entity)
      there was atLeastOne(entity).identity
      repository.resolve(EmptyIdentity).get must_== entity
      repos.flatMap(_.repository.contains(entity)).getOrElse(false) must_== true
    }
    "have stored entity" in {
      val repository = new GenericSyncRepositoryOnMemory[Identity[UUID], EntityImpl]()
      val entity = spy(new EntityImpl(id))
      val repos = repository.store(entity)
      there was atLeastOne(entity).identity
      repository.resolve(id).get must_== entity
      repos.flatMap(_.repository.contains(entity)).getOrElse(false) must_== true
    }
    "resolve a entity by using identity" in {
      val repository = new GenericSyncRepositoryOnMemory[Identity[UUID], EntityImpl]()
      val entity = spy(new EntityImpl(id))
      val repos = repository.store(entity)
      there was atLeastOne(entity).identity
      repository.resolve(id).get must_== entity
      repos.flatMap(_.repository.resolve(id)).get must_== entity
    }
    "resolveOption a entity by using identity" in {
      class TestSyncRepository extends GenericSyncRepositoryOnMemory[Identity[UUID], EntityImpl]
      with SyncRepositoryOnMemorySupportByOption[TestSyncRepository, Identity[UUID], EntityImpl]
      val repository = new TestSyncRepository
      val entity = spy(new EntityImpl(id))
      val repos = repository.store(entity)
      there was atLeastOne(entity).identity
      repository.resolveOption(id).get must_== Some(entity)
      repos.flatMap(_.repository.resolveOption(id)).get must_== Some(entity)
    }
    "delete a entity by using identity" in {
      val repository = new GenericSyncRepositoryOnMemory[Identity[UUID], EntityImpl]()
      val entity = spy(new EntityImpl(id))
      val repos = repository.store(entity)
      there was atLeastOne(entity).identity
      repository.resolve(id).get must_== entity
      repos.flatMap(_.repository.delete(id)).get must_!= repos
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
}
