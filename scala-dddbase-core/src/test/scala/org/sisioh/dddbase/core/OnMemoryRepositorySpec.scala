package org.sisioh.dddbase.core

import java.util.UUID
import org.junit.runner.RunWith
import org.specs2.mock.Mockito
import org.specs2.mutable._
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class OnMemoryRepositorySpec extends Specification with Mockito {

  class EntityImpl(val identity: Identity[UUID]) extends Entity[UUID] with EntityCloneable[UUID, EntityImpl]

  val id = Identity(UUID.randomUUID())

  "The repository" should {
    val repository = new OnMemoryRepository[UUID, EntityImpl]()
    "have stored entity" in {
      val entity = spy(new EntityImpl(id))
      val repos = repository.store(entity)
      there was atLeastOne(entity).identity
      repos.map(_.contains(entity)).getOrElse(false) must_== true
    }
    "resolve entity by using identity" in {
      val entity = spy(new EntityImpl(id))
      val repos = repository.store(entity)
      there was atLeastOne(entity).identity
      repos.flatMap(_.resolve(id)).get must_== entity
    }
    "delete entity by using identity" in {
      val entity = spy(new EntityImpl(id))
      val repos = repository.store(entity)
      there was atLeastOne(entity).identity
      repos.flatMap(_.delete(id)).get must_!= repos
    }
  }

  "The cloned repository" should {
    val repository = new OnMemoryRepository[UUID, EntityImpl]()
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
