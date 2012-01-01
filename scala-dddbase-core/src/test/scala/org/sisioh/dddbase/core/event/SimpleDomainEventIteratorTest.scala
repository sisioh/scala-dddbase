package org.sisioh.dddbase.core.event

import org.scalatest.mock.MockitoSugar
import org.scalatest.junit.AssertionsForJUnit
import org.junit.Test

/**
 * SimpleDomainEventIteratorのためのテスト。
 * @author j5ik2o
 */
class SimpleDomainEventIteratorTest extends AssertionsForJUnit with MockitoSugar {

  @Test
  def testhasNext {
    val sde = new SimpleDomainEventIterator(List.empty[DomainEvent])
    expect(false) {
      sde.hasNext
    }
    intercept[NoSuchElementException] {
      sde.next
    }
  }

  @Test
  def test_peekできること {
    val event1 = mock[DomainEvent]
    val event2 = mock[DomainEvent]
    val sde = new SimpleDomainEventIterator(event1, event2)

    expect(event1)(sde.peek)
    expect(event1)(sde.peek)
  }

  @Test
  def test_nextとhasNextができること {
    val event1 = mock[DomainEvent]
    val event2 = mock[DomainEvent]
    val sde = new SimpleDomainEventIterator(event1, event2)

    assert(sde.hasNext)
    expect(event1)(sde.next)

    assert(sde.hasNext)
    expect(event2)(sde.next)

    assert(sde.hasNext == false)
  }

  @Test
  def test_ReadBeyondEnd {
    val event1 = mock[DomainEvent]
    val sde = new SimpleDomainEventIterator(event1)
    expect(event1)(sde.next)
    intercept[NoSuchElementException] {
      sde.next
    }
  }
}