package org.sisioh.dddbase.core.lifecycle.memory.async

import concurrent.Await
import concurrent.duration.Duration
import java.util.UUID
import org.sisioh.dddbase.core.lifecycle.EntityNotFoundException
import org.sisioh.dddbase.core.model.{EmptyIdentity, Identity, EntityCloneable, Entity}
import org.specs2.mock.Mockito
import org.specs2.mutable._
import scala.concurrent.ExecutionContext.Implicits.global
import org.sisioh.dddbase.core.lifecycle.async.AsyncResultWithEntity

class GenericAsyncRepositoryOnMemorySpec extends Specification with Mockito {

  class EntityImpl(val identity: Identity[UUID])
    extends Entity[Identity[UUID]]
    with EntityCloneable[Identity[UUID], EntityImpl]
    with Ordered[EntityImpl] {

    def compare(that: GenericAsyncRepositoryOnMemorySpec.this.type#EntityImpl): Int = {
      identity.value.compareTo(that.identity.value)
    }

  }

  val id = Identity(UUID.randomUUID)

  "The repository" should {

    "have stored enitty with empty identity" in {
      val repository = new GenericAsyncRepositoryOnMemory[Identity[UUID], EntityImpl]()
      val entity = spy(new EntityImpl(EmptyIdentity))
      repository(entity.identity) = entity
      val future = repository.store(entity).flatMap {
        asyncRepos =>
          asyncRepos.result.contains(EmptyIdentity)
      }
      Await.ready(future, Duration.Inf)
      there was atLeastOne(entity).identity
      Await.result(repository.resolve(EmptyIdentity).recover {
        case ex: EntityNotFoundException => true
      }, Duration.Inf) must_== true
      future.value.get.get must_== true
    }

    "have stored entity" in {
      val repository = new GenericAsyncRepositoryOnMemory[Identity[UUID], EntityImpl]()
      val entity = spy(new EntityImpl(id))
      repository(entity.identity) = entity
      val future = repository.store(entity).flatMap {
        asyncRepos =>
          asyncRepos.result.contains(id)
      }
      Await.ready(future, Duration.Inf)
      there was atLeastOne(entity).identity
      Await.result(repository.resolve(id).recover {
        case ex: EntityNotFoundException => true
      }, Duration.Inf) must_== true
      future.value.get.get must_== true
    }
    "resolve entity by using a identity" in {
      val repository = new GenericAsyncRepositoryOnMemory[Identity[UUID], EntityImpl]()
      val entity = spy(new EntityImpl(id))
      val future = repository.store(entity).flatMap {
        asyncRepos =>
          asyncRepos.result.resolve(id)
      }
      Await.ready(future, Duration.Inf)
      there was atLeastOne(entity).identity
      Await.result(repository.resolve(id).recover {
        case ex: EntityNotFoundException => true
      }, Duration.Inf) must_== true
      future.value.get.get must_== entity
    }
    "delete entity by using a identity" in {
      val repository = new GenericAsyncRepositoryOnMemory[Identity[UUID], EntityImpl]()
      val entity = spy(new EntityImpl(id))
      val future = repository.store(entity).flatMap {
        case AsyncResultWithEntity(repos,_) =>
          repos.delete(id).flatMap {
            case AsyncResultWithEntity(repos,_) =>
              repos.contains(id)
          }
      }
      Await.ready(future, Duration.Inf)
      there was atLeastOne(entity).identity
      Await.result(repository.resolve(id).recover {
        case ex: EntityNotFoundException => true
      }, Duration.Inf) must_== true
      future.value.get.get must_== false
    }
    "not resolve a entity by using a non-existent identity" in {
      val repository = new GenericAsyncRepositoryOnMemory[Identity[UUID], EntityImpl]()
      val future = repository.resolve(id)
      Await.ready(future, Duration.Inf)
      future.value.get.isFailure must_== true
      future.value.get.get must throwA[EntityNotFoundException]
    }
    "not delete a entity by using a non-existent identity" in {
      val repository = new GenericAsyncRepositoryOnMemory[Identity[UUID], EntityImpl]()
      val future = repository.delete(id)
      Await.ready(future, Duration.Inf)
      future.value.get.isFailure must_== true
      future.value.get.get must throwA[EntityNotFoundException]
    }
  }

}
