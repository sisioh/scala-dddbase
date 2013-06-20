package org.sisioh.dddbase.core

import scala.concurrent.{Future, ExecutionContext}

class GenericAsyncOnMemoryRepository[ID <: Identity[_], T <: Entity[ID] with EntityCloneable[ID, T]]
(protected val core: OnMemoryRepository[ID, T] = new GenericOnMemoryRepository[ID, T])
  extends AsyncOnMemoryRepository[ID, T] {

  protected def createInstance(state: OnMemoryRepository[ID, T]): AsyncOnMemoryRepository[ID, T] = {
    new GenericAsyncOnMemoryRepository[ID, T](state)
  }

  override def store(entity: T)(implicit executor: ExecutionContext): Future[GenericAsyncOnMemoryRepository[ID, T]] =
    super.store(entity).map(_.asInstanceOf[GenericAsyncOnMemoryRepository[ID, T]])

  override def delete(entity: T)(implicit executor: ExecutionContext): Future[GenericAsyncOnMemoryRepository[ID, T]] =
    super.delete(entity).map(_.asInstanceOf[GenericAsyncOnMemoryRepository[ID, T]])

}
