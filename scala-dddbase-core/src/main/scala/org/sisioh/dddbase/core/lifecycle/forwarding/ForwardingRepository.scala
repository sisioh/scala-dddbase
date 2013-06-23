package org.sisioh.dddbase.core.lifecycle.forwarding

import org.sisioh.dddbase.core.lifecycle.{EntityWriter, EntityReader, Repository}
import org.sisioh.dddbase.core.model.{Entity, Identity}

trait ForwardingRepository[+R <: Repository[_, ID, T], ID <: Identity[_], T <: Entity[ID]]
  extends ForwardingEntityReader[ID, T] with ForwardingEntityWriter[R, ID, T] with Repository[R, ID, T] {

  protected val delegateRepository: Repository[_, ID, T]
  protected val delegateEntityReader: EntityReader[ID, T] = delegateRepository
  protected val delegateEntityWriter: EntityWriter[_, ID, T] = delegateRepository

}
