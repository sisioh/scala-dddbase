package org.sisioh.dddbase.core

import scala.util.Try

class OnMemoryMutableRepository[ID <: Identity[_], T <: Entity[ID] with EntityCloneable[ID, T]]
  extends Repository[ID, T] with EntityIterableReader[ID, T] with EntityReaderByOption[ID, T] {

  protected var core = new OnMemoryRepository[ID, T]()

  def store(entity: T): Try[OnMemoryMutableRepository[ID, T]] = {
    core.store(entity).map {
      result =>
        core = result
        this
    }
  }

  def delete(identity: ID): Try[OnMemoryMutableRepository[ID, T]] = {
    core.delete(identity).map {
      result =>
        core = result
        this
    }
  }

  def resolveOption(identity: ID): Try[Option[T]] = core.resolveOption(identity)

  def iterator: Iterator[T] = core.iterator

  def resolve(identity: ID): Try[T] = core.resolve(identity)

}
