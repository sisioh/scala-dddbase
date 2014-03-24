package org.sisioh.dddbase.core.lifecycle.async

import org.sisioh.dddbase.core.lifecycle.{ResultWithEntities, EntityIOContext, EntityWriter}
import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.concurrent._
import scala.collection.mutable.ListBuffer

/**
 * 非同期版[[org.sisioh.dddbase.core.lifecycle.EntityWriter]]。
 *
 * @see [[org.sisioh.dddbase.core.lifecycle.EntityWriter]]
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait AsyncEntityWriter[ID <: Identity[_], E <: Entity[ID]]
  extends AsyncEntityIO with EntityWriter[ID, E, Future] {

  type This <: AsyncEntityWriter[ID, E]

  /**
   * エンティティを保存する。
   *
   * @see [[org.sisioh.dddbase.core.lifecycle.Repository]] `store`
   *
   * @param entity 保存する対象のエンティティ
   * @return Success:
   *         リポジトリインスタンスと保存されたエンティティ
   *         Failure:
   *         RepositoryException リポジトリにアクセスできなかった場合
   *         Futureが失敗した場合の例外
   */
  def store(entity: E)(implicit ctx: EntityIOContext[Future]): Future[AsyncResultWithEntity[This, ID, E]]

  /**
   * 複数のタスクを個々のタスクに分解して処理するためのユーティリティメソッド。
   *
   * @param tasks タスクの集合
   * @param processor タスクを処理する関数
   * @param entities 処理したエンティティの集合
   * @param ctx [[org.sisioh.dddbase.core.lifecycle.EntityIOContext]]
   * @tparam A タスクの型
   * @return Success:
   *         リポジトリインスタンスと保存されたエンティティ
   *         Failure
   *         RepositoryExceptionは、リポジトリにアクセスできなかった場合。
   */
  protected final def forEachEntities[A](repository: This, tasks: List[A], entities: ListBuffer[E])
                                        (processor: (This, A) => Future[AsyncResultWithEntity[This, ID, E]])
                                        (implicit ctx: EntityIOContext[Future]): Future[AsyncResultWithEntities[This, ID, E]] = {
    implicit val executor = getExecutionContext(ctx)
    tasks match {
      case Nil =>
        Future.successful(AsyncResultWithEntities[This, ID, E](repository, entities))
      case head :: tail =>
        processor(repository, head).flatMap {
          resultWithEntity =>
            forEachEntities(resultWithEntity.result.asInstanceOf[This], tail, entities :+ resultWithEntity.entity)(processor)
        }
    }
  }

  /**
   * 複数のエンティティを保存する。
   *
   * @param entities 保存する対象のエンティティ
   * @return Success:
   *         リポジトリインスタンスと保存されたエンティティ
   *         Failure
   *         RepositoryExceptionは、リポジトリにアクセスできなかった場合。
   */
  def store(entities: Seq[E])
           (implicit ctx: EntityIOContext[Future]): Future[AsyncResultWithEntities[This, ID, E]] = {
    implicit val executor = getExecutionContext(ctx)
    forEachEntities(this.asInstanceOf[This], entities.toList, ListBuffer[E]()) {
      (repository, entity) =>
        repository.store(entity).asInstanceOf[Future[AsyncResultWithEntity[This, ID, E]]]
    }
  }

  /**
   * 識別子を指定してエンティティを削除する。
   *
   * @param identity 識別子
   * @return Success:
   *         リポジトリインスタンスと削除されたエンティティ
   *         Failure:
   *         RepositoryException リポジトリにアクセスできなかった場合
   *         Futureが失敗した場合の例外
   */
  def deleteByIdentity(identity: ID)(implicit ctx: EntityIOContext[Future]): Future[AsyncResultWithEntity[This, ID, E]]

  /**
   * 指定した複数の識別子のエンティティを削除する。
   *
   * @param identities 識別子
   * @return Success:
   *         リポジトリインスタンスと削除されたエンティティ
   *         Failure:
   *         RepositoryExceptionは、リポジトリにアクセスできなかった場合。
   */
  def deleteByIdentities(identities: Seq[ID])
                        (implicit ctx: EntityIOContext[Future]): Future[AsyncResultWithEntities[This, ID, E]] = {
    implicit val executor = getExecutionContext(ctx)
    forEachEntities(this.asInstanceOf[This], identities.toList, ListBuffer[E]()) {
      (repository, identity) =>
        repository.deleteByIdentity(identity).asInstanceOf[Future[AsyncResultWithEntity[This, ID, E]]]
    }
  }

}
