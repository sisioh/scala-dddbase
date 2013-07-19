package org.sisioh.dddbase.event.lifecycle

/**
 * イベントタイプ。
 */
object EventType extends Enumeration {

  /**
   * 参照
   */
  val Resolve,

  /**
   * 保存　
   */
  Store,


  /**
   * 削除
   */
  Delete = Value

}
