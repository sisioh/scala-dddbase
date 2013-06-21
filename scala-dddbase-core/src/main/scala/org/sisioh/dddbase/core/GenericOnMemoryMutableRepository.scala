package org.sisioh.dddbase.core

class GenericOnMemoryMutableRepository[ID <: Identity[_], T <: Entity[ID] with EntityCloneable[ID, T]]
  extends OnMemoryMutableRepository[GenericOnMemoryMutableRepository[ID, T], ID, T] {

}

