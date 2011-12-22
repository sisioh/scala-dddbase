package org.sisioh.dddbase.core.event

import org.junit.Test
import org.scalatest.junit.AssertionsForJUnit
import org.sisioh.dddbase.core.Identifier
import org.sisioh.dddbase.core.DomainEvent
import org.sisioh.dddbase.core.UUIDIdentifier

/**[[DomainEventSeq]]のためのテスト。
 */
class DomainEventSeqTest extends AssertionsForJUnit {

  class StubDomainEvent
  (val identifier: Identifier, val aggregateId: Identifier)
    extends DomainEvent {

    def this(aggregateId: Identifier) = this (UUIDIdentifier(classOf[StubDomainEvent]), aggregateId)

    val timestamp = System.currentTimeMillis
  }


  //  def testAddEvent_IdAndSequenceNumberInitialized_1 {
  //
  //    val aggregateId = Identifier()
  //    val domainEvent = new StubDomainEvent()
  //
  //    val des1 = DomainEventSeq(aggregateId)
  //
  //    expect(aggregateId)(des1.aggregateId)
  //
  //  }
  //

  @Test
  def testAddEvent_IdAndSequenceNumberInitialized_2 {

    val identifier = UUIDIdentifier(classOf[StubDomainEvent])
    val domainEvent = new StubDomainEvent(identifier)

    val builder = DomainEventSeq.newBuilder(identifier)
    builder.initializeSequenceNumber(11L)

    val des2 = builder.result
    expect(11)(des2.lastSequenceNumber)
    expect(0)(des2.size)
    assert(des2.iterator.hasNext == false)

  }


  @Test
  def testAddEvent_IdAndSequenceNumberInitialized_3 {
    val aggregateId = UUIDIdentifier(classOf[StubDomainEvent])
    val domainEvent = new StubDomainEvent(UUIDIdentifier(classOf[StubDomainEvent]), aggregateId)

    val builder = DomainEventSeq.newBuilder(aggregateId)
    builder.initializeSequenceNumber(11L)

    builder += domainEvent
    expect(Some(12L))(domainEvent.sequenceNumberOption)

    val des = builder.result
    val domainEvent2 = new StubDomainEvent(UUIDIdentifier(classOf[StubDomainEvent]), aggregateId)
    val des2 = des :+ domainEvent2

    expect(1)(des.size)
    expect(Some(13L))(domainEvent2.sequenceNumberOption)
    expect(aggregateId)(domainEvent2.aggregateId)


  }

  //
  //    public void testAddEvent_IdAndSequenceNumberInitialized() {
  //        AggregateIdentifier identifier = new UUIDAggregateIdentifier();
  //        StubDomainEvent domainEvent = new StubDomainEvent();
  //
  //        EventContainer eventContainer = new EventContainer(identifier);
  //        assertEquals(identifier, eventContainer.getAggregateIdentifier());
  //        eventContainer.initializeSequenceNumber(11L);
  //
  //        assertEquals(0, eventContainer.size());
  //        assertFalse(eventContainer.getEventStream().hasNext());
  //
  //        eventContainer.addEvent(domainEvent);
  //
  //        assertEquals(1, eventContainer.size());
  //        assertEquals(new Long(12), domainEvent.getSequenceNumber());
  //        assertEquals(identifier, domainEvent.getAggregateIdentifier());
  //        assertTrue(eventContainer.getEventStream().hasNext());
  //
  //        eventContainer.clear();
  //
  //        assertEquals(0, eventContainer.size());
  //    }
  //
  //    @Test
  //    public void testAddEventWithId_IdConflictsWithContainerId() {
  //        AggregateIdentifier identifier = new UUIDAggregateIdentifier();
  //        StubDomainEvent domainEvent = new StubDomainEvent(identifier);
  //
  //        EventContainer eventContainer = new EventContainer(new UUIDAggregateIdentifier());
  //        eventContainer.initializeSequenceNumber(11L);
  //
  //        try {
  //            eventContainer.addEvent(domainEvent);
  //            fail("Expected IllegalArgumentException");
  //        }
  //        catch (IllegalArgumentException e) {
  //            assertTrue(e.getMessage().toLowerCase().contains("identifier"));
  //            assertTrue(e.getMessage().toLowerCase().contains("match"));
  //        }
  //    }
  //
  //    @Test
  //    public void testAddEvent_SequenceNumberInitialized() {
  //        AggregateIdentifier identifier = new UUIDAggregateIdentifier();
  //        StubDomainEvent domainEvent = new StubDomainEvent(identifier);
  //        StubDomainEvent domainEvent2 = new StubDomainEvent(identifier);
  //        domainEvent.setSequenceNumber(123);
  //
  //        EventContainer eventContainer = new EventContainer(identifier);
  //
  //        eventContainer.addEvent(domainEvent);
  //        eventContainer.addEvent(domainEvent2);
  //
  //        assertEquals(new Long(124), domainEvent2.getSequenceNumber());
  //    }

}