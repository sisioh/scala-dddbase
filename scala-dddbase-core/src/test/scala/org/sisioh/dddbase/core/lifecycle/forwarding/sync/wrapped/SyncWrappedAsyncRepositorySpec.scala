package org.sisioh.dddbase.core.lifecycle.forwarding.sync.wrapped

import org.specs2.mutable.Specification
import org.sisioh.dddbase.core.model.{EmptyIdentity, EntityCloneable, Entity, Identity}
import java.util.UUID
import org.sisioh.dddbase.core.lifecycle.memory.mutable.async.GenericAsyncRepositoryOnMemory
import scala.concurrent.duration.Duration
import org.sisioh.dddbase.core.lifecycle.EntityNotFoundException
import org.specs2.mock.Mockito
import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global
import org.sisioh.dddbase.core.lifecycle.forwarding.async.wrapped.AsyncWrappedSyncEntityIOContext

class SyncWrappedAsyncRepositorySpec extends Specification with Mockito {

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
    extends SyncWrappedAsyncRepository[Identity[UUID], EntityImpl] {

    type This = ForwardingSyncWrappedRepositoryImpl

    type Delegate = GenericAsyncRepositoryOnMemory[Identity[UUID], EntityImpl]

    protected def createInstance(state: (Delegate#This, Option[EntityImpl])): (This, Option[EntityImpl]) = {
      (this.asInstanceOf[This], state._2)
    }

    protected val timeOut: Duration = Duration.Inf
  }

  val id = Identity(UUID.randomUUID)

  implicit val ctx = SyncWrappedAsyncEntityIOContext(AsyncWrappedSyncEntityIOContext())

  "The repository" should {
    "have stored entity with empty identity" in {
      val repository = new ForwardingSyncWrappedRepositoryImpl(GenericAsyncRepositoryOnMemory[Identity[UUID], EntityImpl]())
      val entity = spy(new EntityImpl(EmptyIdentity))
      val repos = repository.store(entity)
      there was atLeastOne(entity).identity
      repository.resolveBy(EmptyIdentity).get must_== entity
      repos.flatMap(_.result.exist(entity)).get must_== true
    }
    "have stored entity" in {
      val repository = new ForwardingSyncWrappedRepositoryImpl(GenericAsyncRepositoryOnMemory[Identity[UUID], EntityImpl]())
      val entity = spy(new EntityImpl(id))
      val repos = repository.store(entity)
      there was atLeastOne(entity).identity
      repository.resolveBy(id).get must_== entity
      repos.flatMap(_.result.exist(entity)).get must_== true
    }
    "resolve a entity by using identity" in {
      val repository = new ForwardingSyncWrappedRepositoryImpl(GenericAsyncRepositoryOnMemory[Identity[UUID], EntityImpl]())
      val entity = spy(new EntityImpl(id))
      val resultWithEntity = repository.store(entity)
      there was atLeastOne(entity).identity
      repository.resolveBy(id).get must_== entity
      resultWithEntity.flatMap(_.result.resolveBy(id)).get must_== entity
    }
    "delete a entity by using identity" in {
      val repository = new ForwardingSyncWrappedRepositoryImpl(GenericAsyncRepositoryOnMemory[Identity[UUID], EntityImpl]())
      val entity = spy(new EntityImpl(id))
      val resultWithEntity = repository.store(entity)
      there was atLeastOne(entity).identity
      repository.resolveBy(id).get must_== entity
      resultWithEntity.flatMap(_.result.deleteBy(id)) must_!= resultWithEntity.get.result
    }
    "fail to resolve a entity by a non-existent identity" in {
      val repository = new ForwardingSyncWrappedRepositoryImpl(GenericAsyncRepositoryOnMemory[Identity[UUID], EntityImpl]())
      repository.resolveBy(id).recover {
        case ex: EntityNotFoundException => true
      }.get must_== true
      repository.resolveBy(id).get must throwA[EntityNotFoundException]
    }
    "fail to delete a entity by a non-existent identity" in {
      val repository = new ForwardingSyncWrappedRepositoryImpl(GenericAsyncRepositoryOnMemory[Identity[UUID], EntityImpl]())
      repository.deleteBy(id).recover {
        case ex: EntityNotFoundException => true
      }.get must_== true
      repository.deleteBy(id).get must throwA[EntityNotFoundException]
    }
  }


}
