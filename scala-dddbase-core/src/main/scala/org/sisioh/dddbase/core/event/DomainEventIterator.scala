/*
 * Copyright 2010 TRICREO, Inc. (http://tricreo.jp/)
 * Copyright 2011 Sisioh Project and others. (http://www.sisioh.org/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.sisioh.dddbase.core.event

import org.sisioh.dddbase.core.DomainEvent

trait DomainEventIterator extends Iterator[DomainEvent] {

  /**
   * このストリームに複数のイベントがある場合にtrueを返す。
   *  `next`メソッドの呼び出しは例外が生じないであろうことを意味する。
   *  もし、このメソッドの呼び出し結果が`false`ならば、連続した`next`メソッドの呼び出し結果について保証がない。
   *
   * @return 複数のイベントがある場合は`true`
   */
  override def hasNext: Boolean

  /**
   * 可能ならばストリーム内の次のイベントを返す。
   * 次のイベントの可能性についての保証を得えるためには`hasNext`メソッドを使うこと。
   * `next`メソッドの呼び出しは、ストリーム内の次のイベントにポインターを進める。
   *
   * もし、ポインターがストリームの最後に到達した場合は、例外をスローする。
   *
   * @return ストリーム内の次のイベント
   * @throws NoSuchElementException 要素がない場合
   */
  override def next: DomainEvent

  /**
   * もし可能ならば、ストリーム内の次のイベントを返す。このメソッドはポインターの移動を必要としない。
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