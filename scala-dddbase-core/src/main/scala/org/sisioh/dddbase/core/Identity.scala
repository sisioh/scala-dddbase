package org.sisioh.dddbase.core

/**
 * エンティティの識別子を表すトレイト。
 *
 * エンティティで用いられる識別子を型として表現することを目的としている。
 *
 * @tparam A 識別子の値を表す型
 */
trait Identity[A] {
  /**
   * 識別子の値を取得する。
   *
   * @return 識別子の値
   */
  def value: A
}

private class IdentityImpl[A](val value: A) extends Identity[A] with Serializable {

  override def toString = s"Identity($value)"

  override def equals(obj: Any) = obj match {
    case that: Identity[_] =>
      value == that.value
    case _ => false
  }

  override def hashCode = 31 * value.##
}

/**
 * コンパニオンオブジェクト。
 */
object Identity {

  /**
   * [[org.sisioh.dddbase.core.Identity]]を生成する。
   *
   * @param value 識別子の値
   * @tparam A 識別子の値の型
   * @return [[org.sisioh.dddbase.core.Identity]]
   */
  def apply[A](value: => A): Identity[A] = new IdentityImpl(value)

  /**
   * 抽出子メソッド。
   *
   * @param v [[org.sisioh.dddbase.core.Identity]]
   * @tparam A 識別子の値の型
   * @return 識別子の値
   */
  def unapply[A](v: Identity[A]): Option[A] = Some(v.value)

}

/**
 * シリアライズに対応したIdentityを実装するためのトレイト。
 *
 * @author mtgto
 */
trait IdentitySerializable[A] extends Identity[A] with Serializable {
  this: Identity[A] =>
}
