package org.sisioh.dddbase.core.lifecycle.forwarding

import java.util.UUID
import org.sisioh.dddbase.core.lifecycle._
import org.sisioh.dddbase.core.lifecycle.memory.GenericOnMemorySyncRepository
import org.sisioh.dddbase.core.model.{EntityCloneable, Entity, Identity}
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import scala.util.Try

class ForwardingRepositorySpec extends Specification with Mockito {

  class EntityImpl(val identity: Identity[UUID])
    extends Entity[Identity[UUID]]
    with EntityCloneable[Identity[UUID], EntityImpl]
    with Ordered[EntityImpl] {

    def compare(that: ForwardingRepositorySpec.this.type#EntityImpl): Int = {
      identity.value.compareTo(that.identity.value)
    }

  }

  val id = Identity(UUID.randomUUID())

  class TestRepForwardingSyncRepositoryImpl
  (protected val delegateRepository: SyncRepository[_, Identity[UUID], EntityImpl])
    extends ForwardingSyncRepository[TestRepForwardingSyncRepositoryImpl, Identity[UUID], EntityImpl] {

    protected def createInstance(state: Try[(SyncEntityWriter[_, Identity[UUID], EntityImpl], Option[EntityImpl])]):
    Try[(TestRepForwardingSyncRepositoryImpl, Option[EntityImpl])] = {
      state.map {
        r =>
          val state = new TestRepForwardingSyncRepositoryImpl(r._1.asInstanceOf[SyncRepository[_, Identity[UUID], EntityImpl]])
          (state, r._2)
      }
    }
  }

  "The repository" should {
    "have stored entity" in {
      val repository = new GenericOnMemorySyncRepository[Identity[UUID], EntityImpl]()
      val entity = spy(new EntityImpl(id))
      val repos = repository.store(entity)
      there was atLeastOne(entity).identity
      repository.resolve(id).isFailure must_== true
      repos.flatMap {
        r =>
          val tr = new TestRepForwardingSyncRepositoryImpl(r.repository)
          tr.contains(entity)
      }.getOrElse(false) must_== true
    }
    "resolve a entity by using identity" in {
      val repository = new GenericOnMemorySyncRepository[Identity[UUID], EntityImpl]()
      val entity = spy(new EntityImpl(id))
      val repos = repository.store(entity)
      there was atLeastOne(entity).identity
      repository.resolve(id).isFailure must_== true
      repos.flatMap {
        r =>
          val tr = new TestRepForwardingSyncRepositoryImpl(r.repository)
          tr.resolve(id)
      }.get must_== entity
    }
    "delete a entity by using identity" in {
      val repository = new GenericOnMemorySyncRepository[Identity[UUID], EntityImpl]()
      val entity = spy(new EntityImpl(id))
      val repos = repository.store(entity)
      there was atLeastOne(entity).identity
      repository.resolve(id).isFailure must_== true
      repos.flatMap {
        r =>
          val tr = new TestRepForwardingSyncRepositoryImpl(r.repository)
          tr.delete(id)
      }.get must_!= repos
    }
    "fail to resolve a entity by a non-existent identity" in {
      val repository = new TestRepForwardingSyncRepositoryImpl(new GenericOnMemorySyncRepository[Identity[UUID], EntityImpl]())
      repository.resolve(id).isFailure must_== true
      repository.resolve(id).get must throwA[EntityNotFoundException]
    }
    "fail to delete a entity by a non-existent identity" in {
      val repository = new TestRepForwardingSyncRepositoryImpl(new GenericOnMemorySyncRepository[Identity[UUID], EntityImpl]())
      repository.delete(id).isFailure must_== true
      repository.delete(id).get must throwA[EntityNotFoundException]
    }
  }

  "The cloned repository" should {
    val repository = new GenericOnMemorySyncRepository[Identity[UUID], EntityImpl]()
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
