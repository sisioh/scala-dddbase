package org.sisioh.dddbase.core

import java.util.UUID
import org.specs2.mock.Mockito
import org.specs2.mutable._
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

class GenericAsyncOnMemoryMutableRepositorySpec extends Specification with Mockito {

  sequential

  class EntityImpl(val identity: Identity[UUID]) extends Entity[Identity[UUID]] with EntityCloneable[Identity[UUID], EntityImpl]

  val id = Identity(UUID.randomUUID())

  "The repository" should {
    val repository = new GenericAsyncOnMemoryMutableRepository[Identity[UUID], EntityImpl]()
    "have stored entity" in {
      val entity = spy(new EntityImpl(id))
      val repos = repository.store(entity)
      there was atLeastOne(entity).identity
      Await.result(repos.flatMap(_.contains(entity)), Duration.Inf) must_== true
    }
    "resolve a entity by using identity" in {
      val entity = spy(new EntityImpl(id))
      val repos = repository.store(entity)
      there was atLeastOne(entity).identity
      Await.result(repos.flatMap(_.resolve(id)), Duration.Inf) must_== entity
    }
    "delete a entity by using identity" in {
      val entity = spy(new EntityImpl(id))
      val repos = repository.store(entity)
      there was atLeastOne(entity).identity
      Await.result(repos.flatMap(_.delete(id)), Duration.Inf) must_!= repos
    }
    "fail to resolve a entity by a non-existent identity" in {
      Await.result(repository.resolve(id).recover {
        case ex: EntityNotFoundException => true
      }, Duration.Inf) must_== true
      Await.result(repository.resolve(id), Duration.Inf) must throwA[EntityNotFoundException]
    }
    "fail to delete a entity by a non-existent identity" in {
      Await.result(repository.delete(id).recover {
        case ex: EntityNotFoundException => true
      }, Duration.Inf) must_== true
      Await.result(repository.delete(id), Duration.Inf) must throwA[EntityNotFoundException]
    }
  }
}
