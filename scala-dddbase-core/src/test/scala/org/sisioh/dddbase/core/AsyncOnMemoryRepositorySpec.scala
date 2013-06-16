package org.sisioh.dddbase.core

import org.specs2.mutable._
import java.util.UUID
import org.specs2.mock.Mockito
import scala.concurrent.ExecutionContext.Implicits.global
import concurrent.Await
import concurrent.duration.Duration

class AsyncOnMemoryRepositorySpec extends Specification with Mockito {

  class EntityImpl(val identity: Identity[UUID]) extends Entity[Identity[UUID]] with EntityCloneable[Identity[UUID], EntityImpl]

  val id = Identity(UUID.randomUUID)

  "The repository" should {
    val repository = new AsyncOnMemoryRepository[Identity[UUID], EntityImpl]()
    "have stored entity" in {
      val entity = spy(new EntityImpl(id))
      repository(entity.identity) = entity
      val future = repository.store(entity).flatMap {
        asyncRepos =>
          asyncRepos.contains(id)
      }
      Await.ready(future, Duration.Inf)
      there was atLeastOne(entity).identity
      future.value.get.get must_== true
    }
    "resolve entity by using a identity" in {
      val entity = spy(new EntityImpl(id))
      val future = repository.store(entity).flatMap {
        asyncRepos =>
          asyncRepos.resolve(id)
      }
      Await.ready(future, Duration.Inf)
      there was atLeastOne(entity).identity
      future.value.get.get must_== entity
    }
    "delete entity by using a identity" in {
      val entity = spy(new EntityImpl(id))
      val future = repository.store(entity).flatMap {
        asyncRepos =>
          asyncRepos.delete(id).flatMap {
            asyncRepos =>
              asyncRepos.contains(id)
          }
      }
      Await.ready(future, Duration.Inf)
      there was atLeastOne(entity).identity
      future.value.get.get must_== false
    }
    "not resolve a entity by using a non-existent identity" in {
      val future = repository.resolve(id)
      Await.ready(future, Duration.Inf)
      future.value.get.isFailure must_== true
      future.value.get.get must throwA[EntityNotFoundException]
    }
    "not delete a entity by using a non-existent identity" in {
      val future = repository.delete(id)
      Await.ready(future, Duration.Inf)
      future.value.get.isFailure must_== true
      future.value.get.get must throwA[EntityNotFoundException]
    }
  }

}
