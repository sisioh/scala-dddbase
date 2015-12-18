package org.sisioh.dddbase.lifecycle.forwarding.async

import java.util.UUID
import org.sisioh.dddbase.core.lifecycle.EntityNotFoundException
import org.sisioh.dddbase.core.lifecycle.async.AsyncRepository
import org.sisioh.dddbase.core.model.{ EntityCloneable, Entity, Identifier }
import org.sisioh.dddbase.lifecycle.forwarding.TestAsyncRepository
import org.sisioh.dddbase.lifecycle.forwarding.async.wrapped.AsyncWrappedSyncEntityIOContext
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{ ExecutionContext, Future, Await }

class ForwardingAsyncRepositorySpec extends Specification with Mockito {

  class EntityImpl(val identifier: Identifier[UUID])
      extends Entity[Identifier[UUID]]
      with EntityCloneable[Identifier[UUID], EntityImpl]
      with Ordered[EntityImpl] {

    def compare(that: EntityImpl): Int = {
      this.identifier.value.compareTo(that.identifier.value)
    }

  }

  val id = Identifier(UUID.randomUUID)

  class TestRepForwardingRepositoryImpl(protected val delegate: AsyncRepository[Identifier[UUID], EntityImpl])(implicit val executor: ExecutionContext)
      extends ForwardingAsyncRepository[Identifier[UUID], EntityImpl] {

    type This = TestRepForwardingRepositoryImpl

    type Delegate = AsyncRepository[Identifier[UUID], EntityImpl]

    protected def createInstance(state: Future[(Delegate#This, Option[EntityImpl])]): Future[(This, Option[EntityImpl])] = {
      state.map {
        r =>
          val state = new TestRepForwardingRepositoryImpl(r._1.asInstanceOf[Delegate#This])
          (state, r._2)
      }
    }

  }

  implicit val ctx = AsyncWrappedSyncEntityIOContext()

  "repository" should {
    "have stored entity" in {
      val repository = new TestAsyncRepository[Identifier[UUID], EntityImpl]()
      val entity = spy(new EntityImpl(id))
      repository(entity.identifier) = entity
      val future = repository.store(entity)
      val resultWithEntity = Await.result(future, Duration.Inf)
      there was atLeastOne(entity).identifier
      (resultWithEntity.result ne repository) must beTrue
      val future2 = future.flatMap {
        r =>
          val tr = new TestRepForwardingRepositoryImpl(r.result)
          tr.resolveBy(id)
      }
      Await.result(future2, Duration.Inf) must_== entity
    }
    "resolve entity by using a identifier" in {
      val repository = new TestAsyncRepository[Identifier[UUID], EntityImpl]()
      val entity = spy(new EntityImpl(id))
      val future = repository.store(entity)
      val resultWithEntity = Await.result(future, Duration.Inf)
      there was atLeastOne(entity).identifier
      (resultWithEntity.result ne repository) must beTrue
      val future2 = future.flatMap {
        r =>
          val tr = new TestRepForwardingRepositoryImpl(r.result)
          tr.resolveBy(id)
      }
      Await.result(future2, Duration.Inf) must_== entity
    }
    "delete entity by using a identifier" in {
      val repository = new TestAsyncRepository[Identifier[UUID], EntityImpl]()
      val entity = spy(new EntityImpl(id))
      val future = repository.store(entity)
      val resultWithEntity = Await.result(future, Duration.Inf)
      there was atLeastOne(entity).identifier
      (resultWithEntity.result ne repository) must beTrue
      val future2 = future.flatMap {
        r =>
          val tr = new TestRepForwardingRepositoryImpl(r.result)
          tr.deleteBy(id)
      }
      Await.result(future2, Duration.Inf) must not beNull
    }
    "not resolve a entity by using a non-existent identifier" in {
      val repository = new TestRepForwardingRepositoryImpl(new TestAsyncRepository[Identifier[UUID], EntityImpl]())
      val future = repository.resolveBy(id)
      Await.ready(future, Duration.Inf)
      future.value.get.isFailure must_== true
      future.value.get.get must throwA[EntityNotFoundException]
    }
    "not delete a entity by using a non-existent identifier" in {
      val repository = new TestRepForwardingRepositoryImpl(new TestAsyncRepository[Identifier[UUID], EntityImpl]())
      val future = repository.deleteBy(id)
      Await.ready(future, Duration.Inf)
      future.value.get.isFailure must_== true
      future.value.get.get must throwA[EntityNotFoundException]
    }
  }

}
