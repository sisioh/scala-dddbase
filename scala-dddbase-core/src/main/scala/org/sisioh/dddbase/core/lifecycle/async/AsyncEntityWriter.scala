package org.sisioh.dddbase.core.lifecycle.async

import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.concurrent.{ExecutionContext, Future}
import org.sisioh.dddbase.core.lifecycle.{EntityWriter, RepositoryWithEntity}

/**
  * 非同期版[[org.sisioh.dddbase.core.lifecycle.EntityWriter]]。
  *
  * @see [[org.sisioh.dddbase.core.lifecycle.EntityWriter]]
  *
  * @tparam ID 識別子の型
  * @tparam T エンティティの型
  */
trait AsyncEntityWriter[+R <: AsyncEntityWriter[_, ID, T], ID <: Identity[_], T <: Entity[ID]]
  extends EntityWriter[R, ID, T, Future] {

     implicit val executor: ExecutionContext

     /**
      * エンティティを保存する。
      *
      * @see [[org.sisioh.dddbase.core.lifecycle.Repository]] `store`
      *
      * @param entity 保存する対象のエンティティ
      * @return Success:
      *         非同期リポジトリ
      *         Failure:
      *         RepositoryException リポジトリにアクセスできなかった場合
      *         Futureが失敗した場合の例外
      */
     def store(entity: T): Future[RepositoryWithEntity[R, T]]

     /**
      * 識別子を指定してエンティティを削除する。
      *
      * @param identity 識別子
      * @return Success:
      *         非同期リポジトリ
      *         Failure:
      *         RepositoryException リポジトリにアクセスできなかった場合
      *         Futureが失敗した場合の例外
      */
     def delete(identity: ID): Future[R]

   }
