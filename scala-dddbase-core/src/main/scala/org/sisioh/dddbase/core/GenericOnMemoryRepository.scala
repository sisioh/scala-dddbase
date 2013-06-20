package org.sisioh.dddbase.core

import scala.util.Try

class GenericOnMemoryRepository[ID <: Identity[_], T <: Entity[ID] with EntityCloneable[ID, T]]
  extends OnMemoryRepository[ID, T] {

  override def store(entity: T): Try[GenericOnMemoryRepository[ID, T]] =
    super.store(entity).map(_.asInstanceOf[GenericOnMemoryRepository[ID, T]])

  override def delete(entity: T): Try[GenericOnMemoryRepository[ID, T]] =
    super.delete(entity).map(_.asInstanceOf[GenericOnMemoryRepository[ID, T]])

  override def clone: GenericOnMemoryRepository[ID, T] =
    super.clone.asInstanceOf[GenericOnMemoryRepository[ID, T]]

}
