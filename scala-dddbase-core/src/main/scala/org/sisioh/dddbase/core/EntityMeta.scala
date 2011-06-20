package org.sisioh.dddbase.core

/**
 * [[org.sisioh.dddbase.core.Entity]]を説明するメタデータ。
 *
 * <p>イミュータブルでなければならない(must)。</p>
 *
 * @tparam T 実体のモデル型
 * @author j5ik2o
 */
trait EntityMeta[T <: Entity] {

  /**
	 * 参照先要素の同一性を調べる。
	 *
	 * @param obj 比較対象
	 * @return 同じIDの要素を参照している場合は{@code true}、そうでない場合は{@code false}
	 */
	def equals(obj: Any):Boolean

  /**
	 * 実体を特定する識別子として[[org.sisioh.dddbase.core.Identifier]]を取得する。
	 *
	 * @return [[org.sisioh.dddbase.core.Identifier]]
	 */
  val identifier: Identifier

  /**
	 * この参照オブジェクトが引数{@code target}の参照かどうか調べる。
	 *
	 * <p>引数の型である[[org.sisioh.dddbase.core.Entity]]型は、
   * T型であるとよりタイプセーフとなるが、
   * [[org.sisioh.dddbase.core.Entity]]型の抽象度で、
   * 「この参照の実体である」ことがチェックできると
	 * 有効なケースがあるため、敢えて[[org.sisioh.dddbase.core.Entity]]型としている。
	 * ただし、偶然同じIDを持ってしまったが型が異なる{@link Entity}が存在してしまった場合は
	 * その後の処理で`ClassCastException`が発生する可能性があるが、
	 * [[java.util.UUID]] の衝突率は無視できるほど小さい。</p>
	 *
	 * @param target 対象{@link Entity}
	 * @return この参照オブジェクトが引数{@code target}の参照の場合は{@code true}、そうでない場合は{@code false}
	 */
  def isReferenceOf(target: Entity): Boolean

}