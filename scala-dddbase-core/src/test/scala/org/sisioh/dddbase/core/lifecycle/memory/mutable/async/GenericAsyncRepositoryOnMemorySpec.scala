package org.sisioh.dddbase.core.lifecycle.memory.mutable.async

import java.util.UUID
import org.sisioh.dddbase.core.lifecycle.EntityNotFoundException
import org.sisioh.dddbase.core.model.{EmptyIdentity, Identity, EntityCloneable, Entity}
import org.specs2.mock.Mockito
import org.specs2.mutable._
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

class GenericAsyncRepositoryOnMemorySpec extends Specification with Mockito {

  sequential

  class EntityImpl(val identity: Identity[UUID])
    extends Entity[Identity[UUID]]
    with EntityCloneable[Identity[UUID], EntityImpl]
    with Ordered[EntityImpl] {
    def compare(that: GenericAsyncRepositoryOnMemorySpec.this.type#EntityImpl): Int = {
      identity.value.compareTo(that.identity.value)
    }
  }

  val id = Identity(UUID.randomUUID())

  "The repository" should {
    "have stored entity with empty identity" in {
      val repository = new GenericAsyncRepositoryOnMemory[Identity[UUID], EntityImpl]()
      val entity = spy(new EntityImpl(EmptyIdentity))
      val repos = repository.store(entity)
      Await.ready(repos, Duration.Inf)
      there was atLeastOne(entity).identity
      Await.result(repository.resolve(EmptyIdentity), Duration.Inf) must_== entity
      Await.result(repos.flatMap(_.result.contains(entity)), Duration.Inf) must_== true
    }
    "have stored entity" in {
      val repository = new GenericAsyncRepositoryOnMemory[Identity[UUID], EntityImpl]()
      val entity = spy(new EntityImpl(id))
      val repos = repository.store(entity)
      Await.ready(repos, Duration.Inf)
      there was atLeastOne(entity).identity
      Await.result(repository.resolve(id), Duration.Inf) must_== entity
      Await.result(repos.flatMap(_.result.contains(entity)), Duration.Inf) must_== true
    }
    "resolve a entity by using identity" in {
      val repository = new GenericAsyncRepositoryOnMemory[Identity[UUID], EntityImpl]()
      val entity = spy(new EntityImpl(id))
      val repos = repository.store(entity)
      Await.ready(repos, Duration.Inf)
      there was atLeastOne(entity).identity
      Await.result(repository.resolve(id), Duration.Inf) must_== entity
      Await.result(repos.flatMap(_.result.resolve(id)), Duration.Inf) must_== entity
    }
    "delete a entity by using identity" in {
      val repository = new GenericAsyncRepositoryOnMemory[Identity[UUID], EntityImpl]()
      val entity = spy(new EntityImpl(id))
      val repos = repository.store(entity)
      Await.ready(repos, Duration.Inf)
      there was atLeastOne(entity).identity
      Await.result(repository.resolve(id), Duration.Inf) must_== entity
      Await.result(repos.flatMap(_.result.delete(id)), Duration.Inf) must_!= repos
    }
    "fail to resolve a entity by a non-existent identity" in {
      val repository = new GenericAsyncRepositoryOnMemory[Identity[UUID], EntityImpl]()
      Await.result(repository.resolve(id).recover {
        case ex: EntityNotFoundException => true
      }, Duration.Inf) must_== true
      Await.result(repository.resolve(id), Duration.Inf) must throwA[EntityNotFoundException]
    }
    "fail to delete a entity by a non-existent identity" in {
      val repository = new GenericAsyncRepositoryOnMemory[Identity[UUID], EntityImpl]()
      Await.result(repository.delete(id).recover {
        case ex: EntityNotFoundException => true
      }, Duration.Inf) must_== true
      Await.result(repository.delete(id), Duration.Inf) must throwA[EntityNotFoundException]
    }
  }
}
