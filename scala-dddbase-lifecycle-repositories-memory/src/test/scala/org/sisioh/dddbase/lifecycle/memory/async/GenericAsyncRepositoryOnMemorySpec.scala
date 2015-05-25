package org.sisioh.dddbase.lifecycle.memory.async

import concurrent.duration.Duration
import org.sisioh.dddbase.core.lifecycle.EntityNotFoundException
import org.sisioh.dddbase.core.lifecycle.async.{ AsyncResultWithEntities, AsyncResultWithEntity }
import org.sisioh.dddbase.core.model._
import org.specs2.mock.Mockito
import org.specs2.mutable._
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global

class GenericAsyncRepositoryOnMemorySpec extends Specification with Mockito {

  sequential

  class EntityImpl(val identifier: Identifier[Int])
      extends Entity[Identifier[Int]]
      with EntityCloneable[Identifier[Int], EntityImpl]
      with Ordered[EntityImpl] {

    def compare(that: GenericAsyncRepositoryOnMemorySpec.this.type#EntityImpl): Int = {
      identifier.value.compareTo(that.identifier.value)
    }

  }

  val id = Identifier(1)

  import GenericAsyncRepositoryOnMemory.Implicits.defaultEntityIOContext

  "The repository" should {

    "have stored enitty with empty identifier" in {
      val repository = new GenericAsyncRepositoryOnMemory[Identifier[Int], EntityImpl]()
      val entity = spy(new EntityImpl(EmptyIdentifier))
      repository(entity.identifier) = entity
      val future = repository.store(entity).flatMap {
        asyncRepos =>
          asyncRepos.result.existBy(EmptyIdentifier)
      }
      Await.ready(future, Duration.Inf)
      there was atLeastOne(entity).identifier
      Await.result(repository.resolveBy(EmptyIdentifier).recover {
        case ex: EntityNotFoundException => true
      }, Duration.Inf) must_== true
      future.value.get.get must_== true
    }
    "have stored entity" in {
      val repository = new GenericAsyncRepositoryOnMemory[Identifier[Int], EntityImpl]()
      val entity = spy(new EntityImpl(id))
      repository(entity.identifier) = entity
      val future = repository.store(entity).flatMap {
        asyncRepos =>
          asyncRepos.result.existBy(id)
      }
      Await.ready(future, Duration.Inf)
      there was atLeastOne(entity).identifier
      Await.result(repository.resolveBy(id).recover {
        case ex: EntityNotFoundException => true
      }, Duration.Inf) must_== true
      future.value.get.get must_== true
    }
    "have stored entities" in {
      val repository = new GenericAsyncRepositoryOnMemory[Identifier[Int], EntityImpl]()
      val entities = for (i <- 0 to 9) yield {
        val id = Identifier(i)
        spy(new EntityImpl(id))
      }
      val future = repository.storeMulti(entities: _*).flatMap {
        resultWithEntities =>
          val identities = resultWithEntities.entities.map(_.identifier)
          resultWithEntities.result.existByMulti(identities: _*)
      }
      Await.ready(future, Duration.Inf)
      for (i <- 0 to 9) {
        there was atLeastOne(entities(i)).identifier
        Await.result(
          repository.resolveBy(entities(i).identifier).recover {
            case ex: EntityNotFoundException => true
          }, Duration.Inf
        ) must_== true
      }
      future.value.get.get must_== true
    }
    "resolve entity by using a identifier" in {
      val repository = new GenericAsyncRepositoryOnMemory[Identifier[Int], EntityImpl]()
      val entity = spy(new EntityImpl(id))
      val future = repository.store(entity).flatMap {
        asyncRepos =>
          asyncRepos.result.resolveBy(id)
      }
      Await.ready(future, Duration.Inf)
      there was atLeastOne(entity).identifier
      Await.result(repository.resolveBy(id).recover {
        case ex: EntityNotFoundException => true
      }, Duration.Inf) must_== true
      future.value.get.get must_== entity
    }
    "resolve entities by using a identifier" in {
      val repository = new GenericAsyncRepositoryOnMemory[Identifier[Int], EntityImpl]()
      val entities = for (i <- 0 to 9) yield {
        val id = Identifier(i)
        spy(new EntityImpl(id))
      }
      val future = repository.storeMulti(entities: _*).flatMap {
        asyncRepos =>
          asyncRepos.result.resolveByMulti(entities.map(_.identifier): _*)
      }
      Await.ready(future, Duration.Inf)
      for (i <- 0 to 9) {
        there was atLeastOne(entities(i)).identifier
        Await.result(
          repository.resolveBy(entities(i).identifier).recover {
            case ex: EntityNotFoundException => true
          }, Duration.Inf
        ) must_== true
      }
      future.value.get.get must_== entities
    }
    "resolve entities by using a identifier" in {
      val repository = new GenericAsyncRepositoryOnMemory[Identifier[Int], EntityImpl]()
      val entities = for (i <- 0 to 9 by 2) yield {
        val id = Identifier(i)
        spy(new EntityImpl(id))
      }
      val future = repository.storeMulti(entities: _*).flatMap {
        asyncRepos =>
          asyncRepos.result.resolveByMulti(entities.map(_.identifier): _*)
      }
      Await.ready(future, Duration.Inf)
      entities.foreach {
        entity =>
          there was atLeastOne(entity).identifier
          Await.result(
            repository.resolveBy(entity.identifier).recover {
              case ex: EntityNotFoundException => true
            }, Duration.Inf
          ) must_== true
      }
      Await.result(repository.resolveByMulti(entities.map(_.identifier): _*), Duration.Inf) must_== Seq.empty
      val r = repository.storeMulti(entities: _*).flatMap {
        resultWithEntities =>
          resultWithEntities.result.resolveByMulti((0 to 9).map(e => Identifier(e)).toSeq: _*)
      }
      Await.result(r, Duration.Inf).size must_!= 0
      future.value.get.get must_== entities
    }
    "delete entity by using a identifier" in {
      val repository = new GenericAsyncRepositoryOnMemory[Identifier[Int], EntityImpl]()
      val entity = spy(new EntityImpl(id))
      val future = repository.store(entity).flatMap {
        case AsyncResultWithEntity(repos1, _) =>
          repos1.deleteBy(id).flatMap {
            case AsyncResultWithEntity(repos2, _) =>
              repos2.existBy(id)
          }
      }
      Await.ready(future, Duration.Inf)
      there was atLeastOne(entity).identifier
      Await.result(
        repository.resolveBy(id).recover {
          case ex: EntityNotFoundException => true
        }, Duration.Inf
      ) must_== true
      future.value.get.get must_== false
    }
    "delete entities by using a identifier" in {
      val repository = new GenericAsyncRepositoryOnMemory[Identifier[Int], EntityImpl]()
      val entities = for (i <- 0 to 9) yield {
        val id = Identifier(i)
        spy(new EntityImpl(id))
      }
      val future = repository.storeMulti(entities: _*).flatMap {
        case AsyncResultWithEntities(repos1, _) =>
          repos1.deleteByMulti(entities.map(_.identifier): _*).flatMap {
            case AsyncResultWithEntities(repos2, _) =>
              repos2.existByMulti(entities.map(_.identifier): _*)
          }
      }
      Await.ready(future, Duration.Inf)
      for (i <- 0 to 9) {
        there was atLeastOne(entities(i)).identifier
        Await.result(
          repository.resolveBy(entities(i).identifier).recover {
            case ex: EntityNotFoundException => true
          }, Duration.Inf
        ) must_== true
      }
      future.value.get.get must_== false
    }
    "not resolve a entity by using a non-existent identifier" in {
      val repository = new GenericAsyncRepositoryOnMemory[Identifier[Int], EntityImpl]()
      val future = repository.resolveBy(id)
      Await.ready(future, Duration.Inf)
      future.value.get.isFailure must_== true
      future.value.get.get must throwA[EntityNotFoundException]
    }
    "not delete a entity by using a non-existent identifier" in {
      val repository = new GenericAsyncRepositoryOnMemory[Identifier[Int], EntityImpl]()
      val future = repository.deleteBy(id)
      Await.ready(future, Duration.Inf)
      future.value.get.isFailure must_== true
      future.value.get.get must throwA[EntityNotFoundException]
    }
  }

}
