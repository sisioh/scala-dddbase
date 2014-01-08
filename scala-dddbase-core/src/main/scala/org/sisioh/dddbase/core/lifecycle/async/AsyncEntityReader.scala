package org.sisioh.dddbase.core.lifecycle.async

import org.sisioh.dddbase.core.lifecycle.EntityReader
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

  protected def traverse[A, R](values: Seq[A], forceSuccess: Boolean)
                              (f: (A) => Future[R])(implicit ctx: Ctx): Future[Seq[R]] = {
    implicit val executor = getExecutionContext(ctx)
    values.map(f).foldLeft(Future.successful(Seq.empty[R])) {
      (resultsFuture, resultFuture) =>
        (for {results <- resultsFuture; result <- resultFuture} yield results :+ result).recoverWith {
          case e => if (forceSuccess) resultsFuture else Future.failed(e)
        }
    }
  }

}
