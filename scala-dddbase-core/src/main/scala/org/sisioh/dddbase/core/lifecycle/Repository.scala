package org.sisioh.dddbase.core.lifecycle

import org.sisioh.dddbase.core.model.{Entity, Identity}

trait Repository[+R <: Repository[_, ID, T, M], ID <: Identity[_], T <: Entity[ID], M[+A]]
  extends EntityReader[ID, T, M] with EntityWriter[R, ID, T, M]

