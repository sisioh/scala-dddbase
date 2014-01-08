package org.sisioh.dddbase.core.lifecycle.memory.async

import concurrent.duration.Duration
import org.sisioh.dddbase.core.lifecycle.EntityNotFoundException
import org.sisioh.dddbase.core.lifecycle.async.{AsyncResultWithEntities, AsyncResultWithEntity}
import org.sisioh.dddbase.core.model.{EmptyIdentity, Identity, EntityCloneable, Entity}
import org.specs2.mock.Mockito
import org.specs2.mutable._
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global

class GenericAsyncRepositoryOnMemorySpec extends Specification with Mockito {

  class EntityImpl(val identity: Identity[Int])
    extends Entity[Identity[Int]]
    with EntityCloneable[Identity[Int], EntityImpl]
    with Ordered[EntityImpl] {

    def compare(that: GenericAsyncRepositoryOnMemorySpec.this.type#EntityImpl): Int = {
      identity.value.compareTo(that.identity.value)
    }

  }

  val id = Identity(1)

  import GenericAsyncRepositoryOnMemory.Implicits.defaultEntityIOContext

  "The repository" should {

    "have stored enitty with empty identity" in {
      val repository = new GenericAsyncRepositoryOnMemory[Identity[Int], EntityImpl]()
      val entity = spy(new EntityImpl(EmptyIdentity))
      repository(entity.identity) = entity
      val future = repository.storeEntity(entity).flatMap {
        asyncRepos =>
          asyncRepos.result.existByIdentifier(EmptyIdentity)
      }
      Await.ready(future, Duration.Inf)
      there was atLeastOne(entity).identity
      Await.result(repository.resolveEntity(EmptyIdentity).recover {
        case ex: EntityNotFoundException => true
      }, Duration.Inf) must_== true
      future.value.get.get must_== true
    }
    "have stored entity" in {
      val repository = new GenericAsyncRepositoryOnMemory[Identity[Int], EntityImpl]()
      val entity = spy(new EntityImpl(id))
      repository(entity.identity) = entity
      val future = repository.storeEntity(entity).flatMap {
        asyncRepos =>
          asyncRepos.result.existByIdentifier(id)
      }
      Await.ready(future, Duration.Inf)
      there was atLeastOne(entity).identity
      Await.result(repository.resolveEntity(id).recover {
        case ex: EntityNotFoundException => true
      }, Duration.Inf) must_== true
      future.value.get.get must_== true
    }
    "have stored entities" in {
      val repository = new GenericAsyncRepositoryOnMemory[Identity[Int], EntityImpl]()
      val entities = for (i <- 0 to 9) yield {
        val id = Identity(i)
        spy(new EntityImpl(id))
      }
      val future = repository.storeEntities(entities).flatMap {
        resultWithEntities =>
          val identities = resultWithEntities.entities.map(_.identity)
          resultWithEntities.result.existByIdentifiers(identities: _*)
      }
      Await.ready(future, Duration.Inf)
      for (i <- 0 to 9) {
        there was atLeastOne(entities(i)).identity
        Await.result(
          repository.resolveEntity(entities(i).identity).recover {
            case ex: EntityNotFoundException => true
          }, Duration.Inf
        ) must_== true
      }
      future.value.get.get must_== true
    }
    "resolve entity by using a identity" in {
      val repository = new GenericAsyncRepositoryOnMemory[Identity[Int], EntityImpl]()
      val entity = spy(new EntityImpl(id))
      val future = repository.storeEntity(entity).flatMap {
        asyncRepos =>
          asyncRepos.result.resolveEntity(id)
      }
      Await.ready(future, Duration.Inf)
      there was atLeastOne(entity).identity
      Await.result(repository.resolveEntity(id).recover {
        case ex: EntityNotFoundException => true
      }, Duration.Inf) must_== true
      future.value.get.get must_== entity
    }
    "resolve entities by using a identity" in {
      val repository = new GenericAsyncRepositoryOnMemory[Identity[Int], EntityImpl]()
      val entities = for (i <- 0 to 9) yield {
        val id = Identity(i)
        spy(new EntityImpl(id))
      }
      val future = repository.storeEntities(entities).flatMap {
        asyncRepos =>
          asyncRepos.result.resolveEntities(entities.map(_.identity))
      }
      Await.ready(future, Duration.Inf)
      for (i <- 0 to 9) {
        there was atLeastOne(entities(i)).identity
        Await.result(
          repository.resolveEntity(entities(i).identity).recover {
            case ex: EntityNotFoundException => true
          }, Duration.Inf
        ) must_== true
      }
      future.value.get.get must_== entities
    }
    "resolve entities by using a identity" in {
      val repository = new GenericAsyncRepositoryOnMemory[Identity[Int], EntityImpl]()
      val entities = for (i <- 0 to 9 by 2) yield {
        val id = Identity(i)
        spy(new EntityImpl(id))
      }
      val future = repository.storeEntities(entities).flatMap {
        asyncRepos =>
          asyncRepos.result.resolveEntities(entities.map(_.identity))
      }
      Await.ready(future, Duration.Inf)
      entities.foreach {
        entity =>
          there was atLeastOne(entity).identity
          Await.result(
            repository.resolveEntity(entity.identity).recover {
              case ex: EntityNotFoundException => true
            }, Duration.Inf
          ) must_== true
      }
      Await.result(repository.resolveEntities(entities.map(_.identity)), Duration.Inf) must_== Seq.empty
      val r = repository.storeEntities(entities).flatMap {
        resultWithEntities =>
          resultWithEntities.result.resolveEntities((0 to 9).map(e => Identity(e)).toSeq)
      }
      Await.result(r, Duration.Inf).size must_!= 0
      future.value.get.get must_== entities
    }
    "delete entity by using a identity" in {
      val repository = new GenericAsyncRepositoryOnMemory[Identity[Int], EntityImpl]()
      val entity = spy(new EntityImpl(id))
      val future = repository.storeEntity(entity).flatMap {
        case AsyncResultWithEntity(repos1, _) =>
          repos1.deleteByIdentifier(id).flatMap {
            case AsyncResultWithEntity(repos2, _) =>
              repos2.existByIdentifier(id)
          }
      }
      Await.ready(future, Duration.Inf)
      there was atLeastOne(entity).identity
      Await.result(
        repository.resolveEntity(id).recover {
          case ex: EntityNotFoundException => true
        }, Duration.Inf
      ) must_== true
      future.value.get.get must_== false
    }
    "delete entities by using a identity" in {
      val repository = new GenericAsyncRepositoryOnMemory[Identity[Int], EntityImpl]()
      val entities = for (i <- 0 to 9) yield {
        val id = Identity(i)
        spy(new EntityImpl(id))
      }
      val future = repository.storeEntities(entities).flatMap {
        case AsyncResultWithEntities(repos1, _) =>
          repos1.deleteByIdentifiers(entities.map(_.identity)).flatMap {
            case AsyncResultWithEntities(repos2, _) =>
              repos2.existByIdentifiers(entities.map(_.identity): _*)
          }
      }
      Await.ready(future, Duration.Inf)
      for (i <- 0 to 9) {
        there was atLeastOne(entities(i)).identity
        Await.result(
          repository.resolveEntity(entities(i).identity).recover {
            case ex: EntityNotFoundException => true
          }, Duration.Inf
        ) must_== true
      }
      future.value.get.get must_== false
    }
    "not resolve a entity by using a non-existent identity" in {
      val repository = new GenericAsyncRepositoryOnMemory[Identity[Int], EntityImpl]()
      val future = repository.resolveEntity(id)
      Await.ready(future, Duration.Inf)
      future.value.get.isFailure must_== true
      future.value.get.get must throwA[EntityNotFoundException]
    }
    "not delete a entity by using a non-existent identity" in {
      val repository = new GenericAsyncRepositoryOnMemory[Identity[Int], EntityImpl]()
      val future = repository.deleteByIdentifier(id)
      Await.ready(future, Duration.Inf)
      future.value.get.isFailure must_== true
      future.value.get.get must throwA[EntityNotFoundException]
    }
  }

}
