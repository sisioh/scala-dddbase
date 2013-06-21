package org.sisioh.dddbase.core

import scala.util.Try

trait OnMemoryMutableRepository
[R <: OnMemoryMutableRepository[R, ID, T],
ID <: Identity[_],
T <: Entity[ID] with EntityCloneable[ID, T]]
  extends OnMemoryRepository[R, ID, T] {

  protected var core: OnMemoryRepository[_, ID, T] = new GenericOnMemoryImmutableRepository[ID, T]()

  def store(entity: T): Try[R] = {
    core.store(entity).map {
      result =>
        core = result.asInstanceOf[OnMemoryRepository[_, ID, T]]
        this.asInstanceOf[R]
    }
  }

  def delete(identity: ID): Try[R] = {
    core.delete(identity).map {
      result =>
        core = result.asInstanceOf[OnMemoryRepository[_, ID, T]]
        this.asInstanceOf[R]
    }
  }

  def resolveOption(identity: ID): Try[Option[T]] = core.resolveOption(identity)

  def iterator: Iterator[T] = core.iterator

  def resolve(identity: ID): Try[T] = core.resolve(identity)

}
