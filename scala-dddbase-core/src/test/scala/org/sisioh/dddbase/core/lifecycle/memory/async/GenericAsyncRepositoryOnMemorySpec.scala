package org.sisioh.dddbase.core.lifecycle.memory.async

import scala.concurrent.{Future, Await}
import concurrent.duration.Duration
import java.util.UUID
import org.sisioh.dddbase.core.lifecycle.{EntityIOContext, EntityNotFoundException}
import org.sisioh.dddbase.core.model.{EmptyIdentity, Identity, EntityCloneable, Entity}
import org.specs2.mock.Mockito
import org.specs2.mutable._
import scala.concurrent.ExecutionContext.Implicits.global
import org.sisioh.dddbase.core.lifecycle.async.{AsyncResultWithEntities, AsyncResultWithEntity}

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

  import GenericAsyncRepositoryOnMemory.Implicits.defaultEntityIOContext

  "The repository" should {

    "have stored enitty with empty identity" in {
      val repository = new GenericAsyncRepositoryOnMemory[EntityIOContext[Future], Identity[UUID], EntityImpl]()
      val entity = spy(new EntityImpl(EmptyIdentity))
      repository(entity.identity) = entity
      val future = repository.store(entity).flatMap {
        asyncRepos =>
          asyncRepos.result.containsByIdentity(EmptyIdentity)
      }
      Await.ready(future, Duration.Inf)
      there was atLeastOne(entity).identity
      Await.result(repository.resolve(EmptyIdentity).recover {
        case ex: EntityNotFoundException => true
      }, Duration.Inf) must_== true
      future.value.get.get must_== true
    }
    "have stored entity" in {
      val repository = new GenericAsyncRepositoryOnMemory[EntityIOContext[Future], Identity[UUID], EntityImpl]()
      val entity = spy(new EntityImpl(id))
      repository(entity.identity) = entity
      val future = repository.store(entity).flatMap {
        asyncRepos =>
          asyncRepos.result.containsByIdentity(id)
      }
      Await.ready(future, Duration.Inf)
      there was atLeastOne(entity).identity
      Await.result(repository.resolve(id).recover {
        case ex: EntityNotFoundException => true
      }, Duration.Inf) must_== true
      future.value.get.get must_== true
    }
    "have stored entities" in {
      val repository = new GenericAsyncRepositoryOnMemory[EntityIOContext[Future], Identity[UUID], EntityImpl]()
      val entities = for (i <- 0 to 9) yield {
        val id = Identity(UUID.randomUUID)
        spy(new EntityImpl(id))
      }
      val future = repository.store(entities).flatMap {
        resultWithEntities =>
          val identities = resultWithEntities.entities.map(_.identity)
          resultWithEntities.result.containsByIdentities(identities)
      }
      Await.ready(future, Duration.Inf)
      for (i <- 0 to 9) {
        there was atLeastOne(entities(i)).identity
        Await.result(
          repository.resolve(entities(i).identity).recover {
            case ex: EntityNotFoundException => true
          }, Duration.Inf
        ) must_== true
      }
      future.value.get.get must_== true
    }
    "resolve entity by using a identity" in {
      val repository = new GenericAsyncRepositoryOnMemory[EntityIOContext[Future], Identity[UUID], EntityImpl]()
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
    "resolve entities by using a identity" in {
      val repository = new GenericAsyncRepositoryOnMemory[EntityIOContext[Future], Identity[UUID], EntityImpl]()
      val entities = for (i <- 0 to 9) yield {
        val id = Identity(UUID.randomUUID)
        spy(new EntityImpl(id))
      }
      val future = repository.store(entities).flatMap {
        asyncRepos =>
          asyncRepos.result.resolves(entities.map(_.identity))
      }
      Await.ready(future, Duration.Inf)
      for (i <- 0 to 9) {
        there was atLeastOne(entities(i)).identity
        Await.result(
          repository.resolve(entities(i).identity).recover {
            case ex: EntityNotFoundException => true
          }, Duration.Inf
        ) must_== true
      }
      future.value.get.get must_== entities
    }
    "delete entity by using a identity" in {
      val repository = new GenericAsyncRepositoryOnMemory[EntityIOContext[Future], Identity[UUID], EntityImpl]()
      val entity = spy(new EntityImpl(id))
      val future = repository.store(entity).flatMap {
        case AsyncResultWithEntity(repos1, _) =>
          repos1.deleteByIdentity(id).flatMap {
            case AsyncResultWithEntity(repos2, _) =>
              repos2.containsByIdentity(id)
          }
      }
      Await.ready(future, Duration.Inf)
      there was atLeastOne(entity).identity
      Await.result(
        repository.resolve(id).recover {
          case ex: EntityNotFoundException => true
        }, Duration.Inf
      ) must_== true
      future.value.get.get must_== false
    }
    "delete entities by using a identity" in {
      val repository = new GenericAsyncRepositoryOnMemory[EntityIOContext[Future], Identity[UUID], EntityImpl]()
      val entities = for (i <- 0 to 9) yield {
        val id = Identity(UUID.randomUUID)
        spy(new EntityImpl(id))
      }
      val future = repository.store(entities).flatMap {
        case AsyncResultWithEntities(repos1, _) =>
          repos1.deleteByIdentities(entities.map(_.identity)).flatMap {
            asyncResultWithEntities =>
              asyncResultWithEntities.result.containsByIdentities(entities.map(_.identity))
          }
      }
      Await.ready(future, Duration.Inf)
      for (i <- 0 to 9) {
        there was atLeastOne(entities(i)).identity
        Await.result(
          repository.resolve(entities(i).identity).recover {
            case ex: EntityNotFoundException => true
          }, Duration.Inf
        ) must_== true
      }
      future.value.get.get must_== false
    }
    "not resolve a entity by using a non-existent identity" in {
      val repository = new GenericAsyncRepositoryOnMemory[EntityIOContext[Future], Identity[UUID], EntityImpl]()
      val future = repository.resolve(id)
      Await.ready(future, Duration.Inf)
      future.value.get.isFailure must_== true
      future.value.get.get must throwA[EntityNotFoundException]
    }
    "not delete a entity by using a non-existent identity" in {
      val repository = new GenericAsyncRepositoryOnMemory[EntityIOContext[Future], Identity[UUID], EntityImpl]()
      val future = repository.deleteByIdentity(id)
      Await.ready(future, Duration.Inf)
      future.value.get.isFailure must_== true
      future.value.get.get must throwA[EntityNotFoundException]
    }
  }

}
