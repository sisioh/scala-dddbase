package org.sisioh.dddbase.core

class GenericOnMemoryImmutableRepository[ID <: Identity[_], T <: Entity[ID] with EntityCloneable[ID, T]]
  extends OnMemoryImmutableRepository[GenericOnMemoryImmutableRepository[ID, T], ID, T]

