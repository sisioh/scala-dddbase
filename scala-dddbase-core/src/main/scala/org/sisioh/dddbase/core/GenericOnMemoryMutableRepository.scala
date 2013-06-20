package org.sisioh.dddbase.core

import scala.util.Try

class GenericOnMemoryMutableRepository[ID <: Identity[_], T <: Entity[ID] with EntityCloneable[ID, T]]
  extends OnMemoryMutableRepository[ID, T] {

  override def store(entity: T): Try[GenericOnMemoryMutableRepository[ID, T]] =
    super.store(entity).map(_.asInstanceOf[GenericOnMemoryMutableRepository[ID, T]])

  override def delete(entity: T): Try[GenericOnMemoryMutableRepository[ID, T]] =
    super.delete(entity).map(_.asInstanceOf[GenericOnMemoryMutableRepository[ID, T]])

}

