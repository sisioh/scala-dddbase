package org.sisioh.dddbase.core.lifecycle.async

import org.sisioh.dddbase.core.lifecycle.{EntityNotFoundException, EntityIOContext, EntityReader}
import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.concurrent.Future

/**
 * 非同期版[[org.sisioh.dddbase.core.lifecycle.EntityReader]]。
 *
 * @see [[org.sisioh.dddbase.core.lifecycle.EntityReader]]
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait AsyncEntityReader[ID <: Identity[_], E <: Entity[ID]]
  extends AsyncEntityIO with EntityReader[ID, E, Future] {

  /**
   * 識別子に該当するエンティティを解決する。
   *
   * @see [[org.sisioh.dddbase.core.lifecycle.EntityReader]] `resolve`
   *
   * @param identity 識別子
   * @return Success:
   *         非同期リポジトリ
   *         Failure:
   *         EntityNotFoundException リポジトリにアクセスできなかった場合
   *         RepositoryException リポジトリにアクセスできなかった場合
   */
  def resolve(identity: ID)(implicit ctx: EntityIOContext[Future]): Future[E]

  protected def traverse[V](values: Seq[V])(f: (V) => Future[E])
                           (implicit ctx: EntityIOContext[Future]): Future[Seq[E]] = {
    implicit val executor = getExecutionContext(ctx)
    values.foldLeft(Future.successful(Seq.empty[E])) {
      (resultFuture, value) =>
        resultFuture.flatMap {
          result =>
            f(value).map(result :+ _).recover {
              case ex: EntityNotFoundException => result
            }
        }
    }
  }

  def resolves(identities: Seq[ID])(implicit ctx: EntityIOContext[Future]): Future[Seq[E]] = {
    traverse(identities)(resolve)
  }

  /**
   * 指定した識別子のエンティティが存在するかを返す。
   *
   * @param identity 識別子
   * @return Success:
   *         存在する場合はtrue
   *         Failure:
   *         EntityNotFoundException リポジトリにアクセスできなかった場合
   *         RepositoryException リポジトリにアクセスできなかった場合
   *         Futureが失敗した場合の例外
   */
  def containsByIdentity(identity: ID)(implicit ctx: EntityIOContext[Future]): Future[Boolean]

  def containsByIdentities(identities: Seq[ID])(implicit ctx: EntityIOContext[Future]): Future[Boolean] = {
    implicit val executor = getExecutionContext(ctx)
    Future.traverse(identities) {
      identity =>
        containsByIdentity(identity)
    }.map {
      contains =>
        contains.forall(_ == true)
    }
  }
}
