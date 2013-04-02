package org.sisioh.dddbase.core

import org.specs2.mutable._
import java.util.UUID
import org.specs2.mock.Mockito
import scala.util._
import scala.concurrent.ExecutionContext.Implicits.global
import concurrent.Await
import concurrent.duration.Duration

class AsyncOnMemoryRepositorySpec extends Specification with Mockito {

  class EntityImpl(val identity: Identity[UUID]) extends Entity[UUID] with EntityCloneable[UUID, EntityImpl]

  val id = Identity(UUID.randomUUID)

  "The repository" should {
    val repository = new AsyncOnMemoryRepository[UUID, EntityImpl]()
    "have stored entity" in {
      val entity = spy(new EntityImpl(id))
      val future = repository.store(entity).flatMap {
        asyncRepos =>
          asyncRepos.contains(id)
      }
      Await.result(future, Duration.Inf)
      there was atLeastOne(entity).identity
      future.value.get.get must_== true
    }
    "resolve entity by using identity" in {
      val entity = spy(new EntityImpl(id))
      val future = repository.store(entity).flatMap {
        asyncRepos =>
          asyncRepos.resolve(id)
      }
      Await.result(future, Duration.Inf)
      there was atLeastOne(entity).identity
      future.value.get.get must_== entity
    }
    "delete entity by using identity" in {
      val entity = spy(new EntityImpl(id))
      val future = repository.store(entity).flatMap {
        asyncRepos =>
          asyncRepos.delete(id).flatMap {
            asyncRepos =>
              asyncRepos.contains(id)
          }
      }
      Await.result(future, Duration.Inf)
      there was atLeastOne(entity).identity
      future.value.get.get must_== false
    }
  }

}
