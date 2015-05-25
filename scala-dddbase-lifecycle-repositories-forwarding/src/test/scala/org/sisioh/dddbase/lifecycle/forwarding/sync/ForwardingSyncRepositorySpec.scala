package org.sisioh.dddbase.lifecycle.forwarding.sync

import java.util.UUID
import org.sisioh.dddbase.core.lifecycle._
import org.sisioh.dddbase.core.lifecycle.sync.{ SyncEntityIOContext, SyncRepository }
import org.sisioh.dddbase.core.model.{ EntityCloneable, Entity, Identifier }
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import scala.util.Try
import org.sisioh.dddbase.lifecycle.forwarding.TestSyncRepository

class ForwardingSyncRepositorySpec extends Specification with Mockito {

  class EntityImpl(val identifier: Identifier[UUID])
      extends Entity[Identifier[UUID]]
      with EntityCloneable[Identifier[UUID], EntityImpl]
      with Ordered[EntityImpl] {

    def compare(that: ForwardingSyncRepositorySpec.this.type#EntityImpl): Int = {
      identifier.value.compareTo(that.identifier.value)
    }

  }

  val id = Identifier(UUID.randomUUID())

  class TestRepForwardingSyncRepositoryImpl(protected val delegate: TestSyncRepository[Identifier[UUID], EntityImpl])
      extends ForwardingSyncRepository[Identifier[UUID], EntityImpl] {

    type This = TestRepForwardingSyncRepositoryImpl

    type Delegate = TestSyncRepository[Identifier[UUID], EntityImpl]

    protected def createInstance(state: Try[(Delegate#This, Option[EntityImpl])]): Try[(TestRepForwardingSyncRepositoryImpl#This, Option[EntityImpl])] = {
      state.map {
        r =>
          val state = new TestRepForwardingSyncRepositoryImpl(r._1.asInstanceOf[TestSyncRepository[Identifier[UUID], EntityImpl]])
          (state, r._2)
      }
    }

  }

  implicit val ctx = SyncEntityIOContext

  "The repository" should {
    "have stored entity" in {
      val repository = new TestSyncRepository[Identifier[UUID], EntityImpl]()
      val entity = spy(new EntityImpl(id))
      val resultWithEntity = repository.store(entity)
      there was atLeastOne(entity).identifier
      repository.resolveBy(id).isFailure must_== true
      (resultWithEntity.get.result ne repository) must beTrue
      resultWithEntity.flatMap {
        r =>
          val tr = new TestRepForwardingSyncRepositoryImpl(r.result)
          tr.exist(entity)
      }.getOrElse(false) must_== true
    }
    "resolve a entity by using identifier" in {
      val repository = new TestSyncRepository[Identifier[UUID], EntityImpl]()
      val entity = spy(new EntityImpl(id))
      val resultWithEntity = repository.store(entity)
      there was atLeastOne(entity).identifier
      (resultWithEntity.get.result ne repository) must beTrue
      repository.resolveBy(id).isFailure must_== true
      resultWithEntity.flatMap {
        r =>
          val tr = new TestRepForwardingSyncRepositoryImpl(r.result)
          tr.resolveBy(id)
      }.get must_== entity
    }
    "delete a entity by using identifier" in {
      val repository = new TestSyncRepository[Identifier[UUID], EntityImpl]()
      val entity = spy(new EntityImpl(id))
      val resultWithEntity = repository.store(entity)
      there was atLeastOne(entity).identifier
      (resultWithEntity.get.result ne repository) must beTrue
      repository.resolveBy(id).isFailure must_== true
      val resultWithEntity2 = resultWithEntity.flatMap {
        r =>
          val tr = new TestRepForwardingSyncRepositoryImpl(r.result)
          tr.deleteBy(id)
      }.get
      resultWithEntity2.result must_!= repository
      resultWithEntity2.entity must_== entity
    }
    "fail to resolve a entity by a non-existent identifier" in {
      val repository = new TestRepForwardingSyncRepositoryImpl(new TestSyncRepository[Identifier[UUID], EntityImpl]())
      repository.resolveBy(id).isFailure must_== true
      repository.resolveBy(id).get must throwA[EntityNotFoundException]
    }
    "fail to delete a entity by a non-existent identifier" in {
      val repository = new TestRepForwardingSyncRepositoryImpl(new TestSyncRepository[Identifier[UUID], EntityImpl]())
      repository.deleteBy(id).isFailure must_== true
      repository.deleteBy(id).get must throwA[EntityNotFoundException]
    }
  }

  //  "The cloned repository" should {
  //    val repository = new GenericSyncRepositoryOnMemory[Identifier[UUID], EntityImpl]()
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
