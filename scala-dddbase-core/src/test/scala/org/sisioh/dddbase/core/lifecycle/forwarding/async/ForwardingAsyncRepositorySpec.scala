package org.sisioh.dddbase.core.lifecycle.forwarding.async

import java.util.UUID
import org.sisioh.dddbase.core.lifecycle.EntityNotFoundException
import org.sisioh.dddbase.core.lifecycle.async.{AsyncEntityWriter, AsyncRepository}
import org.sisioh.dddbase.core.lifecycle.memory.async.GenericAsyncRepositoryOnMemory
import org.sisioh.dddbase.core.model.{EntityCloneable, Entity, Identity}
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{ExecutionContext, Future, Await}

class ForwardingAsyncRepositorySpec extends Specification with Mockito {

  class EntityImpl(val identity: Identity[UUID])
    extends Entity[Identity[UUID]]
    with EntityCloneable[Identity[UUID], EntityImpl]
    with Ordered[EntityImpl] {

    def compare(that: EntityImpl): Int = {
      this.identity.value.compareTo(that.identity.value)
    }

  }

  val id = Identity(UUID.randomUUID)

  class TestRepForwardingRepositoryImplAsync
  (protected val delegateAsyncRepository: AsyncRepository[Identity[UUID], EntityImpl])
  (implicit val executor: ExecutionContext)
    extends ForwardingAsyncRepository[Identity[UUID], EntityImpl] {
    type R = TestRepForwardingRepositoryImplAsync

    protected def createInstance
    (state: Future[(R, Option[EntityImpl])]): Future[(TestRepForwardingRepositoryImplAsync, Option[EntityImpl])] = {
      state.map {
        r =>
          val state = new TestRepForwardingRepositoryImplAsync(r._1.asInstanceOf[AsyncRepository[Identity[UUID], EntityImpl]])
          (state, r._2)
      }
    }

  }

  "repository" should {
    "have stored entity" in {
      val repository = new GenericAsyncRepositoryOnMemory[Identity[UUID], EntityImpl]()
      val entity = spy(new EntityImpl(id))
      repository(entity.identity) = entity
      val future = repository.store(entity)
      Await.ready(future, Duration.Inf)
      there was atLeastOne(entity).identity
      val future2 = future.flatMap {
        r =>
          val tr = new TestRepForwardingRepositoryImplAsync(r.repository)
          tr.resolve(id)
      }
      Await.result(future2, Duration.Inf) must_== entity
    }
    "resolve entity by using a identity" in {
      val repository = new GenericAsyncRepositoryOnMemory[Identity[UUID], EntityImpl]()
      val entity = spy(new EntityImpl(id))
      val future = repository.store(entity)
      Await.ready(future, Duration.Inf)
      there was atLeastOne(entity).identity
      val future2 = future.flatMap {
        r =>
          val tr = new TestRepForwardingRepositoryImplAsync(r.repository)
          tr.resolve(id)
      }
      Await.result(future2, Duration.Inf) must_== entity
    }
    "delete entity by using a identity" in {
      val repository = new GenericAsyncRepositoryOnMemory[Identity[UUID], EntityImpl]()
      val entity = spy(new EntityImpl(id))
      val future = repository.store(entity)
      Await.ready(future, Duration.Inf)
      there was atLeastOne(entity).identity
      val future2 = future.flatMap {
        r =>
          val tr = new TestRepForwardingRepositoryImplAsync(r.repository)
          tr.delete(id)
      }
      Await.result(future2, Duration.Inf) must not beNull
    }
    "not resolve a entity by using a non-existent identity" in {
      val repository = new TestRepForwardingRepositoryImplAsync(new GenericAsyncRepositoryOnMemory[Identity[UUID], EntityImpl]())
      val future = repository.resolve(id)
      Await.ready(future, Duration.Inf)
      future.value.get.isFailure must_== true
      future.value.get.get must throwA[EntityNotFoundException]
    }
    "not delete a entity by using a non-existent identity" in {
      val repository = new TestRepForwardingRepositoryImplAsync(new GenericAsyncRepositoryOnMemory[Identity[UUID], EntityImpl]())
      val future = repository.delete(id)
      Await.ready(future, Duration.Inf)
      future.value.get.isFailure must_== true
      future.value.get.get must throwA[EntityNotFoundException]
    }
  }

}
