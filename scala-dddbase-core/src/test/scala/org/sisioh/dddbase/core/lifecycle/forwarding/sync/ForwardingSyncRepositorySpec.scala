package org.sisioh.dddbase.core.lifecycle.forwarding.sync

import java.util.UUID
import org.sisioh.dddbase.core.lifecycle._
import org.sisioh.dddbase.core.lifecycle.memory.sync.GenericSyncRepositoryOnMemory
import org.sisioh.dddbase.core.lifecycle.sync.{SyncEntityIOContext, SyncRepository}
import org.sisioh.dddbase.core.model.{EntityCloneable, Entity, Identity}
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import scala.util.Try

class ForwardingSyncRepositorySpec extends Specification with Mockito {

  class EntityImpl(val identity: Identity[UUID])
    extends Entity[Identity[UUID]]
    with EntityCloneable[Identity[UUID], EntityImpl]
    with Ordered[EntityImpl] {

    def compare(that: ForwardingSyncRepositorySpec.this.type#EntityImpl): Int = {
      identity.value.compareTo(that.identity.value)
    }

  }

  val id = Identity(UUID.randomUUID())

  class TestRepForwardingSyncRepositoryImpl
  (protected val delegate: SyncRepository[Identity[UUID], EntityImpl])
    extends ForwardingSyncRepository[Identity[UUID], EntityImpl] {

    type This = TestRepForwardingSyncRepositoryImpl

    type Delegate = SyncRepository[Identity[UUID], EntityImpl]

    protected def createInstance(state: Try[(Delegate#This, Option[EntityImpl])]): Try[(TestRepForwardingSyncRepositoryImpl#This, Option[EntityImpl])] = {
      state.map {
        r =>
          val state = new TestRepForwardingSyncRepositoryImpl(r._1.asInstanceOf[SyncRepository[Identity[UUID], EntityImpl]])
          (state, r._2)
      }
    }

  }

  implicit val ctx = SyncEntityIOContext


  "The repository" should {
    "have stored entity" in {
      val repository = new GenericSyncRepositoryOnMemory[Identity[UUID], EntityImpl]()
      val entity = spy(new EntityImpl(id))
      val resultWithEntity = repository.storeEntity(entity)
      there was atLeastOne(entity).identity
      repository.resolveEntity(id).isFailure must_== true
      (resultWithEntity.get.result ne repository) must beTrue
      resultWithEntity.flatMap {
        r =>
          val tr = new TestRepForwardingSyncRepositoryImpl(r.result)
          tr.existByEntity(entity)
      }.getOrElse(false) must_== true
    }
    "resolve a entity by using identity" in {
      val repository = new GenericSyncRepositoryOnMemory[Identity[UUID], EntityImpl]()
      val entity = spy(new EntityImpl(id))
      val resultWithEntity = repository.storeEntity(entity)
      there was atLeastOne(entity).identity
      (resultWithEntity.get.result ne repository) must beTrue
      repository.resolveEntity(id).isFailure must_== true
      resultWithEntity.flatMap {
        r =>
          val tr = new TestRepForwardingSyncRepositoryImpl(r.result)
          tr.resolveEntity(id)
      }.get must_== entity
    }
    "delete a entity by using identity" in {
      val repository = new GenericSyncRepositoryOnMemory[Identity[UUID], EntityImpl]()
      val entity = spy(new EntityImpl(id))
      val resultWithEntity = repository.storeEntity(entity)
      there was atLeastOne(entity).identity
      (resultWithEntity.get.result ne repository) must beTrue
      repository.resolveEntity(id).isFailure must_== true
      val resultWithEntity2 = resultWithEntity.flatMap {
        r =>
          val tr = new TestRepForwardingSyncRepositoryImpl(r.result)
          tr.deleteByIdentifier(id)
      }.get
      resultWithEntity2.result must_!= repository
      resultWithEntity2.entity must_== entity
    }
    "fail to resolve a entity by a non-existent identity" in {
      val repository = new TestRepForwardingSyncRepositoryImpl(new GenericSyncRepositoryOnMemory[Identity[UUID], EntityImpl]())
      repository.resolveEntity(id).isFailure must_== true
      repository.resolveEntity(id).get must throwA[EntityNotFoundException]
    }
    "fail to delete a entity by a non-existent identity" in {
      val repository = new TestRepForwardingSyncRepositoryImpl(new GenericSyncRepositoryOnMemory[Identity[UUID], EntityImpl]())
      repository.deleteByIdentifier(id).isFailure must_== true
      repository.deleteByIdentifier(id).get must throwA[EntityNotFoundException]
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
