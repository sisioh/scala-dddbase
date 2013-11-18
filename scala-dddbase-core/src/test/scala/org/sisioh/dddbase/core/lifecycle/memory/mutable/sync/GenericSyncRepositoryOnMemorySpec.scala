package org.sisioh.dddbase.core.lifecycle.memory.mutable.sync

import java.util.UUID
import org.sisioh.dddbase.core.lifecycle.{EntityIOContext, EntityNotFoundException}
import org.sisioh.dddbase.core.lifecycle.memory.mutable.sync._
import org.sisioh.dddbase.core.model.{EmptyIdentity, Identity, EntityCloneable, Entity}
import org.specs2.mock.Mockito
import org.specs2.mutable._
import scala.Some
import org.sisioh.dddbase.core.lifecycle.async.AsyncEntityIOContext
import org.sisioh.dddbase.core.lifecycle.sync.SyncEntityIOContext
import scala.util.Try

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

  import GenericSyncRepositoryOnMemory.Implicits.defaultEntityIOContext

  "The repository" should {
    "have stored entity with empty identity" in {
      val repository = new GenericSyncRepositoryOnMemory[EntityIOContext[Try], Identity[UUID], EntityImpl]()
      val entity = spy(new EntityImpl(EmptyIdentity))
      val repos = repository.store(entity)
      there was atLeastOne(entity).identity
      repository.resolve(EmptyIdentity).get must_== entity
      repos.flatMap(_.result.contains(entity)).getOrElse(false) must_== true
    }
    "have stored entity" in {
      val repository = new GenericSyncRepositoryOnMemory[EntityIOContext[Try], Identity[UUID], EntityImpl]()
      val entity = spy(new EntityImpl(id))
      val repos = repository.store(entity)
      there was atLeastOne(entity).identity
      repository.resolve(id).get must_== entity
      repos.flatMap(_.result.contains(entity)).getOrElse(false) must_== true
    }
    "resolve a entity by using identity" in {
      val repository = new GenericSyncRepositoryOnMemory[EntityIOContext[Try], Identity[UUID], EntityImpl]()
      val entity = spy(new EntityImpl(id))
      val repos = repository.store(entity)
      there was atLeastOne(entity).identity
      repository.resolve(id).get must_== entity
      repos.flatMap(_.result.resolve(id)).get must_== entity
    }
    "resolveOption a entity by using identity" in {
      class TestSyncRepository
        extends SyncRepositoryOnMemorySupport[EntityIOContext[Try], Identity[UUID], EntityImpl]
        with SyncRepositoryOnMemorySupportByOption[EntityIOContext[Try], Identity[UUID], EntityImpl] {
        type This = TestSyncRepository
      }
      val repository = new TestSyncRepository
      val entity = spy(new EntityImpl(id))
      val repos = repository.store(entity)
      there was atLeastOne(entity).identity
      repository.resolveOption(id).get must_== Some(entity)
      repos.flatMap(_.result.resolveOption(id)).get must_== Some(entity)
    }
    "delete a entity by using identity" in {
      val repository = new GenericSyncRepositoryOnMemory[EntityIOContext[Try], Identity[UUID], EntityImpl]()
      val entity = spy(new EntityImpl(id))
      val repos = repository.store(entity)
      there was atLeastOne(entity).identity
      repository.resolve(id).get must_== entity
      val resultWithEntity = repos.flatMap(_.result.deleteByIdentity(id)).get
      resultWithEntity.result must_!= repos
      resultWithEntity.entity must_== entity
    }
    "fail to resolve a entity by a non-existent identity" in {
      val repository = new GenericSyncRepositoryOnMemory[EntityIOContext[Try], Identity[UUID], EntityImpl]()
      repository.resolve(id).isFailure must_== true
      repository.resolve(id).get must throwA[EntityNotFoundException]
    }
    "fail to delete a entity by a non-existent identity" in {
      val repository = new GenericSyncRepositoryOnMemory[EntityIOContext[Try], Identity[UUID], EntityImpl]()
      repository.deleteByIdentity(id).isFailure must_== true
      repository.deleteByIdentity(id).get must throwA[EntityNotFoundException]
    }
  }
}
