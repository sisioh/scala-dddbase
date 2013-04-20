package org.sisioh.dddbase.core

import scala.concurrent._
import scala.util._
import ExecutionContext.Implicits.global
import scala.util.control.NonFatal

class AsyncOnMemoryRepository[ID <: Identity[_], T <: Entity[ID] with EntityCloneable[ID, T]]
(private val core: OnMemoryRepository[ID, T] = new OnMemoryRepository[ID, T]())
  extends AsyncRepository[ID, T] {

  def resolve(identifier: ID) = future {
    core.resolve(identifier).get
  }

  def resolveOption(identifier: ID) = future {
    core.resolveOption(identifier)
  }

  def contains(identifier: ID) = future {
    core.contains(identifier).get
  }

  def store(entity: T) = future {
    new AsyncOnMemoryRepository(core.store(entity).get)
  }

  def delete(identity: ID) = future {
    new AsyncOnMemoryRepository(core.delete(identity).get)
  }

}
