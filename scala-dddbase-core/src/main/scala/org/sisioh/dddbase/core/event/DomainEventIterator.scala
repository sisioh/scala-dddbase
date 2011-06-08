package org.sisioh.dddbase.core.event

import org.sisioh.dddbase.core.DomainEvent

trait DomainEventIterator extends Iterator[DomainEvent]{

  /**このストリームに複数のイベントがある場合にtrueを返す。
   *  `next`メソッドの呼び出しは例外が生じないであろうことを意味する。
   *  もし、このメソッドの呼び出し結果が`false`ならば、連続した`next`メソッドの呼び出し結果について保証がない。
   *
   * @return 複数のイベントがある場合は`true`
   */
  override def hasNext: Boolean

  /**可能ならばストリーム内の次のイベントを返す。
   * 次のイベントの可能性についての保証を得えるためには`hasNext`メソッドを使うこと。
   * `next`メソッドの呼び出しは、ストリーム内の次のイベントにポインターを進める。
   *
   * もし、ポインターがストリームの最後に到達した場合は、例外をスローする。
   *
   * @return ストリーム内の次のイベント
   * @throws NoSuchElementException 要素がない場合
   */
  override def next: DomainEvent

  /**もし可能ならば、ストリーム内の次のイベントを返す。このメソッドはポインターの移動を必要としない。
   * それゆえに、`next`メソッドの呼び出しは`peek`メソッドの呼び出しと同じイベントが返される。
   * 次のイベントの可能性についての保証を得えるためには`hasNext`メソッドを使うこと。
   *
   * もし、ポインターがストリームの最後に到達した場合は、例外をスローする。
   *
   * @return ストリーム内の次のイベント
   * @throws NoSuchElementException 要素がない場合
   */
  def peek: DomainEvent
}