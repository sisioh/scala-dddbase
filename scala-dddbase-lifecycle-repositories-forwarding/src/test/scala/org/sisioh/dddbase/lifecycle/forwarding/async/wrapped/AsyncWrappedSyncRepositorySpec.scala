package org.sisioh.dddbase.lifecycle.forwarding.async.wrapped

import java.util.UUID
import org.sisioh.dddbase.core.model._
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{ Await, ExecutionContext }
import org.sisioh.dddbase.core.lifecycle.EntityNotFoundException
import org.sisioh.dddbase.core.lifecycle.sync.{ SyncResultWithEntity, SyncRepository }
import scala.util.Try
import org.sisioh.dddbase.lifecycle.forwarding.TestSyncMutableRepository

class AsyncWrappedSyncRepositorySpec extends Specification with Mockito {

  class EntityImpl(val identifier: Identifier[UUID])
      extends Entity[Identifier[UUID]]
      with EntityCloneable[Identifier[UUID], EntityImpl]
      with Ordered[EntityImpl] {
    def compare(that: EntityImpl): Int = {
      identifier.value.compareTo(that.identifier.value)
    }
  }

  class ForwardingAsyncWrappedRepositoryImpl(protected val delegate: TestSyncMutableRepository[Identifier[UUID], EntityImpl])(implicit val executor: ExecutionContext)
      extends AsyncWrappedSyncRepository[Identifier[UUID], EntityImpl] {

    type This = ForwardingAsyncWrappedRepositoryImpl

    type Delegate = TestSyncMutableRepository[Identifier[UUID], EntityImpl]

    protected def createInstance(state: (Delegate#This, Option[EntityImpl])): (This, Option[EntityImpl]) = {
      (this.asInstanceOf[This], state._2)
    }

  }

  val id = Identifier(UUID.randomUUID)

  implicit val ctx = AsyncWrappedSyncEntityIOContext()

  "The repository" should {
    "have stored entity with empty identifier" in {
      val repository = new ForwardingAsyncWrappedRepositoryImpl(TestSyncMutableRepository())
      val entity = spy(new EntityImpl(EmptyIdentifier))
      val repos = repository.store(entity)
      Await.ready(repos, Duration.Inf)
      there was atLeastOne(entity).identifier
      Await.result(repository.resolveBy(EmptyIdentifier), Duration.Inf) must_== entity
      Await.result(repos.flatMap(_.result.exist(entity)), Duration.Inf) must_== true
    }
    "have stored entity" in {
      val repository = new ForwardingAsyncWrappedRepositoryImpl(TestSyncMutableRepository())
      val entity = spy(new EntityImpl(id))
      val repos = repository.store(entity)
      Await.ready(repos, Duration.Inf)
      there was atLeastOne(entity).identifier
      Await.result(repository.resolveBy(id), Duration.Inf) must_== entity
      Await.result(repos.flatMap(_.result.exist(entity)), Duration.Inf) must_== true
    }
    "resolve a entity by using identifier" in {
      val repository = new ForwardingAsyncWrappedRepositoryImpl(TestSyncMutableRepository())
      val entity = spy(new EntityImpl(id))
      val repos = repository.store(entity)
      Await.ready(repos, Duration.Inf)
      there was atLeastOne(entity).identifier
      Await.result(repository.resolveBy(id), Duration.Inf) must_== entity
      Await.result(repos.flatMap(_.result.resolveBy(id)), Duration.Inf) must_== entity
    }
    "delete a entity by using identifier" in {
      val repository = new ForwardingAsyncWrappedRepositoryImpl(TestSyncMutableRepository())
      val entity = spy(new EntityImpl(id))
      val repos = repository.store(entity)
      Await.ready(repos, Duration.Inf)
      there was atLeastOne(entity).identifier
      Await.result(repository.resolveBy(id), Duration.Inf) must_== entity
      Await.result(repos.flatMap(_.result.deleteBy(id)), Duration.Inf) must_!= repos
    }
    "fail to resolve a entity by a non-existent identifier" in {
      val repository = new ForwardingAsyncWrappedRepositoryImpl(TestSyncMutableRepository())
      Await.result(repository.resolveBy(id).recover {
        case ex: EntityNotFoundException => true
      }, Duration.Inf) must_== true
      Await.result(repository.resolveBy(id), Duration.Inf) must throwA[EntityNotFoundException]
    }
    "fail to delete a entity by a non-existent identifier" in {
      val repository = new ForwardingAsyncWrappedRepositoryImpl(TestSyncMutableRepository())
      Await.result(repository.deleteBy(id).recover {
        case ex: EntityNotFoundException => true
      }, Duration.Inf) must_== true
      Await.result(repository.deleteBy(id), Duration.Inf) must throwA[EntityNotFoundException]
    }
  }
}
