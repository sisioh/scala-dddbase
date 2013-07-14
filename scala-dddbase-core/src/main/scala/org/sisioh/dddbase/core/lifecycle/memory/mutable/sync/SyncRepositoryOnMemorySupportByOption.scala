package org.sisioh.dddbase.core.lifecycle.memory.mutable.sync

import org.sisioh.dddbase.core.lifecycle.EntityNotFoundException
import org.sisioh.dddbase.core.lifecycle.sync._
import org.sisioh.dddbase.core.model._
import scala.util._

/**
  * [[org.sisioh.dddbase.core.lifecycle.memory.mutable.sync.SyncRepositoryOnMemorySupport]]にOption型のサポートを追加するトレイト。
  *
  * @tparam R 当該リポジトリを実装する派生型
  * @tparam ID エンティティの識別子の型
  * @tparam T エンティティの型
  */
trait SyncRepositoryOnMemorySupportByOption
[+R <: SyncRepository[_, ID, T],
 ID <: Identity[_],
 T <: Entity[ID] with EntityCloneable[ID, T] with Ordered[T]]
  extends SyncRepositoryOnMemorySupport[R, ID, T]
     with SyncEntityReaderByOption[ID, T] {

     def resolveOption(identity: ID): Try[Option[T]] = synchronized {
       resolve(identity).map(Some(_)).recoverWith {
         case ex: EntityNotFoundException =>
           Success(None)
       }
     }

   }
