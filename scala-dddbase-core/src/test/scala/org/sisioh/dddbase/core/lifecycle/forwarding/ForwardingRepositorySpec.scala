package org.sisioh.dddbase.core.lifecycle.forwarding

import java.util.UUID
import org.sisioh.dddbase.core.lifecycle._
import org.sisioh.dddbase.core.lifecycle.memory.GenericOnMemoryRepository
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

  class TestRepForwardingRepositoryImpl
  (protected val delegateRepository: Repository[_, Identity[UUID], EntityImpl])
    extends ForwardingRepository[TestRepForwardingRepositoryImpl, Identity[UUID], EntityImpl] {

    protected def createInstance(state: Try[(EntityWriter[_, Identity[UUID], EntityImpl], Option[EntityImpl])]):
    Try[(TestRepForwardingRepositoryImpl, Option[EntityImpl])] = {
      state.map {
        r =>
          val state = new TestRepForwardingRepositoryImpl(r._1.asInstanceOf[Repository[_, Identity[UUID], EntityImpl]])
          (state, r._2)
      }
    }
  }

  "The repository" should {
    "have stored entity" in {
      val repository = new GenericOnMemoryRepository[Identity[UUID], EntityImpl]()
      val entity = spy(new EntityImpl(id))
      val repos = repository.store(entity)
      there was atLeastOne(entity).identity
      repository.resolve(id).isFailure must_== true
      repos.flatMap {
        r =>
          val tr = new TestRepForwardingRepositoryImpl(r.state)
          tr.contains(entity)
      }.getOrElse(false) must_== true
    }
    "resolve a entity by using identity" in {
      val repository = new GenericOnMemoryRepository[Identity[UUID], EntityImpl]()
      val entity = spy(new EntityImpl(id))
      val repos = repository.store(entity)
      there was atLeastOne(entity).identity
      repository.resolve(id).isFailure must_== true
      repos.flatMap {
        r =>
          val tr = new TestRepForwardingRepositoryImpl(r.state)
          tr.resolve(id)
      }.get must_== entity
    }
    "delete a entity by using identity" in {
      val repository = new GenericOnMemoryRepository[Identity[UUID], EntityImpl]()
      val entity = spy(new EntityImpl(id))
      val repos = repository.store(entity)
      there was atLeastOne(entity).identity
      repository.resolve(id).isFailure must_== true
      repos.flatMap {
        r =>
          val tr = new TestRepForwardingRepositoryImpl(r.state)
          tr.delete(id)
      }.get must_!= repos
    }
    "fail to resolve a entity by a non-existent identity" in {
      val repository = new TestRepForwardingRepositoryImpl(new GenericOnMemoryRepository[Identity[UUID], EntityImpl]())
      repository.resolve(id).isFailure must_== true
      repository.resolve(id).get must throwA[EntityNotFoundException]
    }
    "fail to delete a entity by a non-existent identity" in {
      val repository = new TestRepForwardingRepositoryImpl(new GenericOnMemoryRepository[Identity[UUID], EntityImpl]())
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
