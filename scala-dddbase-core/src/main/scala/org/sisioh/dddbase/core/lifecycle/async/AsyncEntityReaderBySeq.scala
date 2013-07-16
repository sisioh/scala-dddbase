package org.sisioh.dddbase.core.lifecycle.async

import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.concurrent.{Future, ExecutionContext}

/**
  * 非同期ですべてのエンティティを取得するためのトレイト。
  *
  * @tparam ID 識別子の型
  * @tparam T エンティティの型
  */
trait AsyncEntityReaderBySeq[ID <: Identity[_], T <: Entity[ID]] {
   this: AsyncEntityReader[ID, T] =>

   /**
    * すべてのエンティティを取得する。
    *
    * @return Success:
    *         エンティティの列
    *         Failure:
    *         RepositoryExceptionは、リポジトリにアクセスできなかった場合。
    */
   def resolveAll: Future[Seq[T]]

 }
