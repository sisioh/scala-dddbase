package org.sisioh.dddbase.core.lifecycle.forwarding

import java.util.UUID
import org.sisioh.dddbase.core.lifecycle.memory.GenericAsyncOnMemoryRepository
import org.sisioh.dddbase.core.lifecycle.{AsyncEntityWriter, AsyncRepository, EntityNotFoundException}
import org.sisioh.dddbase.core.model.{EntityCloneable, Entity, Identity}
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Future, Await}

class AsyncForwardingRepositorySpec extends Specification with Mockito {

  class EntityImpl(val identity: Identity[UUID])
    extends Entity[Identity[UUID]]
    with EntityCloneable[Identity[UUID], EntityImpl]
    with Ordered[EntityImpl] {

    def compare(that: EntityImpl): Int = {
      this.identity.value.compareTo(that.identity.value)
    }

  }

  val id = Identity(UUID.randomUUID)

  class TestRepAsyncForwardingRepositoryImpl
  (protected val delegateAsyncRepository: AsyncRepository[_, Identity[UUID], EntityImpl])
    extends AsyncForwardingRepository[TestRepAsyncForwardingRepositoryImpl, Identity[UUID], EntityImpl] {

    protected def createInstance
    (state: Future[(AsyncEntityWriter[_, Identity[UUID], EntityImpl], Option[EntityImpl])]): Future[(TestRepAsyncForwardingRepositoryImpl, Option[EntityImpl])] = {
      state.map{
        r =>
          val state = new TestRepAsyncForwardingRepositoryImpl(r._1.asInstanceOf[AsyncRepository[_, Identity[UUID], EntityImpl]])
          (state, r._2)
      }
    }
  }

  "repository" should {
    "have stored entity" in {
      val repository = new GenericAsyncOnMemoryRepository[Identity[UUID], EntityImpl]()
      val entity = spy(new EntityImpl(id))
      repository(entity.identity) = entity
      val future = repository.store(entity)
      Await.ready(future, Duration.Inf)
      there was atLeastOne(entity).identity
      val future2 = future.flatMap {
        r =>
          val tr = new TestRepAsyncForwardingRepositoryImpl(r.repository)
          tr.resolve(id)
      }
      Await.result(future2, Duration.Inf) must_== entity
    }
    "resolve entity by using a identity" in {
      val repository = new GenericAsyncOnMemoryRepository[Identity[UUID], EntityImpl]()
      val entity = spy(new EntityImpl(id))
      val future = repository.store(entity)
      Await.ready(future, Duration.Inf)
      there was atLeastOne(entity).identity
      val future2 = future.flatMap {
        r =>
          val tr = new TestRepAsyncForwardingRepositoryImpl(r.repository)
          tr.resolve(id)
      }
      Await.result(future2, Duration.Inf) must_== entity
    }
    "delete entity by using a identity" in {
      val repository = new GenericAsyncOnMemoryRepository[Identity[UUID], EntityImpl]()
      val entity = spy(new EntityImpl(id))
      val future = repository.store(entity)
      Await.ready(future, Duration.Inf)
      there was atLeastOne(entity).identity
      val future2 = future.flatMap {
        r =>
          val tr = new TestRepAsyncForwardingRepositoryImpl(r.repository)
          tr.delete(id)
      }
      Await.result(future2, Duration.Inf) must not beNull
    }
    "not resolve a entity by using a non-existent identity" in {
      val repository = new TestRepAsyncForwardingRepositoryImpl(new GenericAsyncOnMemoryRepository[Identity[UUID], EntityImpl]())
      val future = repository.resolve(id)
      Await.ready(future, Duration.Inf)
      future.value.get.isFailure must_== true
      future.value.get.get must throwA[EntityNotFoundException]
    }
    "not delete a entity by using a non-existent identity" in {
      val repository = new TestRepAsyncForwardingRepositoryImpl(new GenericAsyncOnMemoryRepository[Identity[UUID], EntityImpl]())
      val future = repository.delete(id)
      Await.ready(future, Duration.Inf)
      future.value.get.isFailure must_== true
      future.value.get.get must throwA[EntityNotFoundException]
    }
  }

}
