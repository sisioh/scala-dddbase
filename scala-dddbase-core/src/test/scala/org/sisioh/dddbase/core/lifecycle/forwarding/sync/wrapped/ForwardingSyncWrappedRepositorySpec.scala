package org.sisioh.dddbase.core.lifecycle.forwarding.sync.wrapped

import org.specs2.mutable.Specification
import org.sisioh.dddbase.core.model.{EmptyIdentity, EntityCloneable, Entity, Identity}
import java.util.UUID
import scala.concurrent.ExecutionContext.Implicits.global
import org.sisioh.dddbase.core.lifecycle.memory.mutable.async.GenericAsyncRepositoryOnMemory
import scala.concurrent.duration.Duration
import org.sisioh.dddbase.core.lifecycle.EntityNotFoundException
import org.specs2.mock.Mockito
import scala.concurrent.ExecutionContext

/**
 * Created with IntelliJ IDEA.
 * User: junichi.kato
 * Date: 2013/08/06
 * Time: 14:14
 * To change this template use File | Settings | File Templates.
 */
class ForwardingSyncWrappedRepositorySpec extends Specification with Mockito {

  class EntityImpl(val identity: Identity[UUID])
    extends Entity[Identity[UUID]]
    with EntityCloneable[Identity[UUID], EntityImpl]
    with Ordered[EntityImpl] {
    def compare(that: EntityImpl): Int = {
      identity.value.compareTo(that.identity.value)
    }
  }

  class ForwardingSyncWrappedRepositoryImpl
  (protected val delegate: GenericAsyncRepositoryOnMemory[Identity[UUID], EntityImpl])
  (implicit val executor: ExecutionContext)
    extends ForwardingSyncWrappedRepository[Identity[UUID], EntityImpl] {

    type This = ForwardingSyncWrappedRepositoryImpl

    type Delegate = GenericAsyncRepositoryOnMemory[Identity[UUID], EntityImpl]

    protected def createInstance(state: (Delegate#This, Option[EntityImpl])): (This, Option[EntityImpl]) = {
      (this.asInstanceOf[This], state._2)
    }

    protected val timeOut: Duration = Duration.Inf
  }

  val id = Identity(UUID.randomUUID)

  "The repository" should {
    "have stored entity with empty identity" in {
      val repository = new ForwardingSyncWrappedRepositoryImpl(GenericAsyncRepositoryOnMemory[Identity[UUID], EntityImpl]())
      val entity = spy(new EntityImpl(EmptyIdentity))
      val repos = repository.store(entity)
      there was atLeastOne(entity).identity
      repository.resolve(EmptyIdentity).get must_== entity
      repos.flatMap(_.result.contains(entity)).get must_== true
    }
    "have stored entity" in {
      val repository = new ForwardingSyncWrappedRepositoryImpl(GenericAsyncRepositoryOnMemory[Identity[UUID], EntityImpl]())
      val entity = spy(new EntityImpl(id))
      val repos = repository.store(entity)
      there was atLeastOne(entity).identity
      repository.resolve(id).get must_== entity
      repos.flatMap(_.result.contains(entity)).get must_== true
    }
    "resolve a entity by using identity" in {
      val repository = new ForwardingSyncWrappedRepositoryImpl(GenericAsyncRepositoryOnMemory[Identity[UUID], EntityImpl]())
      val entity = spy(new EntityImpl(id))
      val resultWithEntity = repository.store(entity)
      there was atLeastOne(entity).identity
      repository.resolve(id).get must_== entity
      resultWithEntity.flatMap(_.result.resolve(id)).get must_== entity
    }
    "delete a entity by using identity" in {
      val repository = new ForwardingSyncWrappedRepositoryImpl(GenericAsyncRepositoryOnMemory[Identity[UUID], EntityImpl]())
      val entity = spy(new EntityImpl(id))
      val resultWithEntity = repository.store(entity)
      there was atLeastOne(entity).identity
      repository.resolve(id).get must_== entity
      resultWithEntity.flatMap(_.result.delete(id)) must_!= resultWithEntity.get.result
    }
    "fail to resolve a entity by a non-existent identity" in {
      val repository = new ForwardingSyncWrappedRepositoryImpl(GenericAsyncRepositoryOnMemory[Identity[UUID], EntityImpl]())
      repository.resolve(id).recover {
        case ex: EntityNotFoundException => true
      }.get must_== true
      repository.resolve(id).get must throwA[EntityNotFoundException]
    }
    "fail to delete a entity by a non-existent identity" in {
      val repository = new ForwardingSyncWrappedRepositoryImpl(GenericAsyncRepositoryOnMemory[Identity[UUID], EntityImpl]())
      repository.delete(id).recover {
        case ex: EntityNotFoundException => true
      }.get must_== true
      repository.delete(id).get must throwA[EntityNotFoundException]
    }
  }


}
