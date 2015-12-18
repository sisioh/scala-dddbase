package org.sisioh.dddbase.lifecycle.memory.sync

import org.sisioh.dddbase.core.lifecycle.EntityNotFoundException
import org.sisioh.dddbase.core.model._
import org.specs2.mock.Mockito
import org.specs2.mutable._

class GenericSyncRepositoryOnMemorySpec extends Specification with Mockito {

  class EntityImpl(val identifier: Identifier[Int])
      extends Entity[Identifier[Int]]
      with EntityCloneable[Identifier[Int], EntityImpl]
      with Ordered[EntityImpl] {
    def compare(that: GenericSyncRepositoryOnMemorySpec.this.type#EntityImpl): Int = {
      identifier.value.compareTo(that.identifier.value)
    }
  }

  val id = Identifier(1)

  import GenericSyncRepositoryOnMemory.Implicits.defaultEntityIOContext

  "The repository" should {
    "have stored enitty with empty identifier" in {
      val repository = new GenericSyncRepositoryOnMemory[Identifier[Int], EntityImpl]()
      val entity = spy(new EntityImpl(EmptyIdentifier))
      val resultWithEntity = repository.store(entity)
      there was atLeastOne(entity).identifier
      repository.resolveBy(id).isFailure must_== true
      resultWithEntity.flatMap(_.result.exist(entity)).getOrElse(false) must_== true
      (resultWithEntity.get.result ne repository) must beTrue
    }
    "have stored entity" in {
      val repository = new GenericSyncRepositoryOnMemory[Identifier[Int], EntityImpl]()
      val entity = spy(new EntityImpl(id))
      val repos = repository.store(entity)
      there was atLeastOne(entity).identifier
      repository.resolveBy(id).isFailure must_== true
      repos.flatMap(_.result.exist(entity)).getOrElse(false) must_== true
    }
    "have stored entities" in {
      val repository = new GenericSyncRepositoryOnMemory[Identifier[Int], EntityImpl]()
      val entities = for (i <- 0 to 9) yield {
        val id = Identifier(i)
        spy(new EntityImpl(id))
      }
      val repos = repository.storeMulti(entities: _*)
      entities must contain { (e: Entity[Identifier[Int]]) =>
        there was atLeastOne(e).identifier
        repository.resolveBy(e.identifier).isFailure must_== true
        repos.map(_.entities.contains(e)).getOrElse(false) must_== true
      }.forall
    }
    "resolve a entity by using identifier" in {
      val repository = new GenericSyncRepositoryOnMemory[Identifier[Int], EntityImpl]()
      val entity = spy(new EntityImpl(id))
      val repos = repository.store(entity)
      there was atLeastOne(entity).identifier
      repository.resolveBy(id).isFailure must_== true
      repos.flatMap(_.result.resolveBy(id)).get must_== entity
    }
    "resolve a entities by using identifier" in {
      val repository = new GenericSyncRepositoryOnMemory[Identifier[Int], EntityImpl]()
      val entities = for (i <- 0 to 9) yield {
        val id = Identifier(i)
        spy(new EntityImpl(id))
      }
      val repos = repository.storeMulti(entities: _*)
      entities must contain((e: Entity[Identifier[Int]]) =>
        there was atLeastOne(e).identifier
      ).forall
      repository.resolveByMulti(entities.map(_.identifier): _*).isSuccess must_== true
      val _entities = repos.flatMap(_.result.resolveByMulti(entities.map(_.identifier): _*)).get
      _entities must_== entities
    }
    "resolve a entities by using identifier" in {
      val repository = new GenericSyncRepositoryOnMemory[Identifier[Int], EntityImpl]()
      val entities = for (i <- 0 to 9 by 2) yield {
        val id = Identifier(i)
        spy(new EntityImpl(id))
      }
      val repos = repository.storeMulti(entities: _*)
      entities must contain((e: Entity[Identifier[Int]]) =>
        there was atLeastOne(e).identifier
      ).forall
      repository.resolveByMulti((0 to 9 by 2).map(Identifier(_)).toSeq: _*).isSuccess must_== true
      repos.get.result.resolveByMulti((0 to 9).map(Identifier(_)).toSeq: _*).isSuccess must_== true
      val _entities = repos.flatMap(_.result.resolveByMulti(entities.map(_.identifier): _*)).get
      _entities must_== entities
    }
    "resolveOption a entity by using identifier" in {
      type ID = Identifier[Int]
      class TestSyncRepository(entities: Map[Identifier[Int], EntityImpl] = Map.empty)
          extends AbstractSyncRepositoryOnMemory[ID, EntityImpl](entities)
          with SyncRepositoryOnMemorySupportAsOption[ID, EntityImpl] {
        type This = TestSyncRepository

        override protected def createInstance(entities: Map[Identifier[Int], EntityImpl]): This =
          new TestSyncRepository(entities)
      }
      val repository = new TestSyncRepository
      val entity = spy(new EntityImpl(id))
      val repos = repository.store(entity)
      there was atLeastOne(entity).identifier
      val resolveOptionTry = repos.map {
        r =>
          r.result.resolveAsOptionBy(id)
      }.get
      resolveOptionTry.get must_== entity
    }
    "delete a entity by using identifier" in {
      val repository = new GenericSyncRepositoryOnMemory[Identifier[Int], EntityImpl]()
      val entity = spy(new EntityImpl(id))
      val resultWithEntity = repository.store(entity)
      there was atLeastOne(entity).identifier
      repository.resolveBy(id).isFailure must_== true
      val resultWithEntity2 = resultWithEntity.flatMap(_.result.deleteBy(id)).get
      (resultWithEntity2.result ne repository) must beTrue
      resultWithEntity2.entity must_== entity
    }
    "delete a entities by using identifier" in {
      val repository = new GenericSyncRepositoryOnMemory[Identifier[Int], EntityImpl]()
      val entities = for (i <- 0 to 9) yield {
        val id = Identifier(i)
        spy(new EntityImpl(id))
      }
      val resultWithEntity = repository.storeMulti(entities: _*)
      entities must contain { (e: Entity[Identifier[Int]]) =>
        there was atLeastOne(e).identifier
        repository.resolveBy(e.identifier).isFailure must_== true
      }.forall
      val resultWithEntity2 = resultWithEntity.flatMap {
        _.result.deleteByMulti(entities.map(_.identifier): _*)
      }.get
      (resultWithEntity2.result ne repository) must beTrue
      resultWithEntity2.entities must_== entities
    }
    "fail to resolve a entity by a non-existent identifier" in {
      val repository = new GenericSyncRepositoryOnMemory[Identifier[Int], EntityImpl]()
      repository.resolveBy(id).isFailure must_== true
      repository.resolveBy(id).get must throwA[EntityNotFoundException]
    }
    "fail to delete a entity by a non-existent identifier" in {
      val repository = new GenericSyncRepositoryOnMemory[Identifier[Int], EntityImpl]()
      repository.deleteBy(id).isFailure must_== true
      repository.deleteBy(id).get must throwA[EntityNotFoundException]
    }
  }

  //  "The cloned repository" should {
  //    val repository = new GenericSyncRepositoryOnMemory[Identifier[Int], EntityImpl]()
  //    "equals the repository before clone" in {
  //      repository must_== repository.clone
  //    }
  //    "have unequal values to the repository before clone" in {
  //      val cloneRepository = repository.clone
  //      val r = repository.entities ne cloneRepository.entities
  //      r must beTrue
  //    }
  //  }
}
