package org.sisioh.dddbase.core

case class EntitiesChunk[ID <: Identity[_], E <: Entity[ID]](index: Int, entities: Seq[E])

