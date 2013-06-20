package org.sisioh.dddbase.core

import java.util.UUID
import org.specs2.mock.Mockito
import org.specs2.mutable._

class GenericOnMemoryRepositorySpec extends Specification with Mockito {

  class EntityImpl(val identity: Identity[UUID]) extends Entity[Identity[UUID]] with EntityCloneable[Identity[UUID], EntityImpl]

  val id = Identity(UUID.randomUUID())

  "The repository" should {
    val repository = new GenericOnMemoryRepository[Identity[UUID], EntityImpl]()
    "have stored entity" in {
      val entity = spy(new EntityImpl(id))
      val repos = repository.store(entity)
      there was atLeastOne(entity).identity
      repos.flatMap(_.contains(entity)).getOrElse(false) must_== true
    }
    "resolve a entity by using identity" in {
      val entity = spy(new EntityImpl(id))
      val repos = repository.store(entity)
      there was atLeastOne(entity).identity
      repos.flatMap(_.resolve(id)).get must_== entity
    }
    "delete a entity by using identity" in {
      val entity = spy(new EntityImpl(id))
      val repos = repository.store(entity)
      there was atLeastOne(entity).identity
      repos.flatMap(_.delete(id)).get must_!= repos
    }
    "fail to resolve a entity by a non-existent identity" in {
      repository.resolve(id).isFailure must_== true
      repository.resolve(id).get must throwA[EntityNotFoundException]
    }
    "fail to delete a entity by a non-existent identity" in {
      repository.delete(id).isFailure must_== true
      repository.delete(id).get must throwA[EntityNotFoundException]
    }
  }

  "The cloned repository" should {
    val repository = new GenericOnMemoryRepository[Identity[UUID], EntityImpl]()
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
