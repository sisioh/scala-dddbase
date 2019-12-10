package org.sisioh.dddbase.lifecycle.memory.sync

import org.sisioh.dddbase.core.lifecycle.EntityNotFoundException
import org.sisioh.dddbase.core.model._
import org.mockito.Mockito._
import org.mockito.Mockito.{atLeastOnce => leastOnce}
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers._
import org.scalatest.Inspectors.forAll

class GenericSyncRepositoryOnMemorySpec extends AnyFreeSpec {

  class EntityImpl(val identifier: Identifier[Int])
      extends Entity[Identifier[Int]]
      with EntityCloneable[Identifier[Int], EntityImpl]
      with Ordered[EntityImpl] {
    def compare(
      that: GenericSyncRepositoryOnMemorySpec.this.type#EntityImpl
    ): Int = {
      identifier.value.compareTo(that.identifier.value)
    }
  }

  val id = Identifier(1)

  import GenericSyncRepositoryOnMemory.Implicits.defaultEntityIOContext

  "The repository" - {
    "have stored enitty with empty identifier" in {
      val repository =
        new GenericSyncRepositoryOnMemory[Identifier[Int], EntityImpl]()
      val entity = spy(new EntityImpl(EmptyIdentifier))
      val resultWithEntity = repository.store(entity)
      verify(entity, leastOnce()).identifier
      repository.resolveBy(id).isFailure shouldBe true
      resultWithEntity
        .flatMap(_.result.exist(entity))
        .getOrElse(false) shouldBe true
      (resultWithEntity.get.result ne repository) shouldBe true
    }
    "have stored entity" in {
      val repository =
        new GenericSyncRepositoryOnMemory[Identifier[Int], EntityImpl]()
      val entity = spy(new EntityImpl(id))
      val repos = repository.store(entity)
      verify(entity, leastOnce()).identifier
      repository.resolveBy(id).isFailure shouldBe true
      repos.flatMap(_.result.exist(entity)).getOrElse(false) shouldBe true
    }
    //TODO: これより下で使われるforAllの変更箇所が元と同じ意味なのか要確認
    "have stored entities" in {
      val repository =
        new GenericSyncRepositoryOnMemory[Identifier[Int], EntityImpl]()
      val entities = for (i <- 0 to 9) yield {
        val id = Identifier(i)
        spy(new EntityImpl(id))
      }
      val repos = repository.storeMulti(entities: _*)
      forAll(entities) { (e: Entity[Identifier[Int]]) =>
        verify(e, leastOnce()).identifier
        repository.resolveBy(e.identifier).isFailure shouldBe true
        repos.map(_.entities.contains(e)).getOrElse(false) shouldBe true
      }
    }
    "resolve a entity by using identifier" in {
      val repository =
        new GenericSyncRepositoryOnMemory[Identifier[Int], EntityImpl]()
      val entity = spy(new EntityImpl(id))
      val repos = repository.store(entity)
      verify(entity, leastOnce()).identifier
      repository.resolveBy(id).isFailure shouldBe true
      repos.flatMap(_.result.resolveBy(id)).get shouldBe entity
    }

    "resolve a entities by using identifier" in {
      val repository =
        new GenericSyncRepositoryOnMemory[Identifier[Int], EntityImpl]()
      val entities = for (i <- 0 to 9) yield {
        val id = Identifier(i)
        spy(new EntityImpl(id))
      }
      val repos = repository.storeMulti(entities: _*)
      forAll(entities) { e: Entity[Identifier[Int]] =>
        verify(e, leastOnce()).identifier
      }
      repository
        .resolveByMulti(entities.map(_.identifier): _*)
        .isSuccess shouldBe true
      val _entities = repos
        .flatMap(_.result.resolveByMulti(entities.map(_.identifier): _*))
        .get
      _entities shouldBe entities
    }
    //FIXME "error" Duplicate test name
    "resolve a entities by using identifier2" in {
      val repository =
        new GenericSyncRepositoryOnMemory[Identifier[Int], EntityImpl]()
      val entities = for (i <- 0 to 9 by 2) yield {
        val id = Identifier(i)
        spy(new EntityImpl(id))
      }
      val repos = repository.storeMulti(entities: _*)
      forAll(entities) { (e: Entity[Identifier[Int]]) =>
        verify(e, leastOnce()).identifier
      }
      repository
        .resolveByMulti((0 to 9 by 2).map(Identifier(_)).toSeq: _*)
        .isSuccess shouldBe true
      repos.get.result
        .resolveByMulti((0 to 9).map(Identifier(_)).toSeq: _*)
        .isSuccess shouldBe true
      val _entities = repos
        .flatMap(_.result.resolveByMulti(entities.map(_.identifier): _*))
        .get
      _entities shouldBe entities
    }
    "resolveOption a entity by using identifier" in {
      type ID = Identifier[Int]
      class TestSyncRepository(
        entities: Map[Identifier[Int], EntityImpl] = Map.empty
      ) extends AbstractSyncRepositoryOnMemory[ID, EntityImpl](entities)
          with SyncRepositoryOnMemorySupportAsOption[ID, EntityImpl] {
        type This = TestSyncRepository

        override protected def createInstance(
          entities: Map[Identifier[Int], EntityImpl]
        ): This =
          new TestSyncRepository(entities)
      }
      val repository = new TestSyncRepository
      val entity = spy(new EntityImpl(id))
      val repos = repository.store(entity)
      verify(entity, leastOnce()).identifier
      val resolveOptionTry = repos.map { r =>
        r.result.resolveAsOptionBy(id)
      }.get
      resolveOptionTry.get shouldBe entity
    }
    "delete a entity by using identifier" in {
      val repository =
        new GenericSyncRepositoryOnMemory[Identifier[Int], EntityImpl]()
      val entity = spy(new EntityImpl(id))
      val resultWithEntity = repository.store(entity)
      verify(entity, leastOnce()).identifier
      repository.resolveBy(id).isFailure shouldBe true
      val resultWithEntity2 =
        resultWithEntity.flatMap(_.result.deleteBy(id)).get
      (resultWithEntity2.result ne repository) shouldBe true
      resultWithEntity2.entity shouldBe entity
    }
    "delete a entities by using identifier" in {
      val repository =
        new GenericSyncRepositoryOnMemory[Identifier[Int], EntityImpl]()
      val entities = for (i <- 0 to 9) yield {
        val id = Identifier(i)
        spy(new EntityImpl(id))
      }
      val resultWithEntity = repository.storeMulti(entities: _*)
      forAll(entities) { (e: Entity[Identifier[Int]]) =>
        verify(e, leastOnce()).identifier
        repository.resolveBy(e.identifier).isFailure shouldBe true
      }
      val resultWithEntity2 = resultWithEntity.flatMap {
        _.result.deleteByMulti(entities.map(_.identifier): _*)
      }.get
      (resultWithEntity2.result ne repository) shouldBe true
      resultWithEntity2.entities shouldBe entities
    }
    "fail to resolve a entity by a non-existent identifier" in {
      val repository =
        new GenericSyncRepositoryOnMemory[Identifier[Int], EntityImpl]()
      repository.resolveBy(id).isFailure shouldBe true
      a[EntityNotFoundException] shouldBe thrownBy(repository.resolveBy(id).get)
    }
    "fail to delete a entity by a non-existent identifier" in {
      val repository =
        new GenericSyncRepositoryOnMemory[Identifier[Int], EntityImpl]()
      repository.deleteBy(id).isFailure shouldBe true
      a[EntityNotFoundException] shouldBe thrownBy(repository.deleteBy(id).get)
    }
  }

  //  "The cloned repository" should {
  //    val repository = new GenericSyncRepositoryOnMemory[Identifier[Int], EntityImpl]()
  //    "equals the repository before clone" in {
  //      repository shouldBe repository.clone
  //    }
  //    "have unequal values to the repository before clone" in {
  //      val cloneRepository = repository.clone
  //      val r = repository.entities ne cloneRepository.entities
  //      r must beTrue
  //    }
  //  }
}
