package org.sisioh.dddbase.core.event

import org.junit.Test
import org.scalatest.junit.AssertionsForJUnit
import scalaz.Identity
import java.util.UUID

/**[[org.sisioh.dddbase.core.event.DomainEventSeq]]のためのテスト。
 */
class DomainEventSeqTest extends AssertionsForJUnit {

  class StubDomainEvent
  (val identity: Identity[UUID], val aggregateIdentity: Identity[UUID])
    extends DomainEvent {

    def this(aggregateId: Identity[UUID]) = this (Identity(UUID.randomUUID()), aggregateId)

    val timestamp = System.currentTimeMillis
  }


  //  def testAddEvent_IdAndSequenceNumberInitialized_1 {
  //
  //    val aggregateIdentity = Identifier()
  //    val domainEvent = new StubDomainEvent()
  //
  //    val des1 = DomainEventSeq(aggregateIdentity)
  //
  //    expect(aggregateIdentity)(des1.aggregateIdentity)
  //
  //  }
  //

  @Test
  def testAddEvent_IdAndSequenceNumberInitialized_2 {

    val identifier = Identity(UUID.randomUUID())
    val domainEvent = new StubDomainEvent(identifier)

    val builder = DomainEventSeq.newBuilder(identifier)
    builder.initializeSequenceNumber(11L)

    val des2 = builder.result
    expectResult(11)(des2.lastSequenceNumber)
    expectResult(0)(des2.size)
    assert(des2.iterator.hasNext == false)

  }


  @Test
  def testAddEvent_IdAndSequenceNumberInitialized_3 {
    val aggregateId = Identity(UUID.randomUUID())
    val domainEvent = new StubDomainEvent(Identity(UUID.randomUUID()), aggregateId)

    val builder = DomainEventSeq.newBuilder(aggregateId)
    builder.initializeSequenceNumber(11L)

    builder += domainEvent
    expectResult(Some(12L))(domainEvent.sequenceNumberOption)

    val des = builder.result
    val domainEvent2 = new StubDomainEvent(Identity(UUID.randomUUID()), aggregateId)
    val des2 = des :+ domainEvent2

    expectResult(1)(des.size)
    expectResult(Some(13L))(domainEvent2.sequenceNumberOption)
    expectResult(aggregateId)(domainEvent2.aggregateIdentity)


  }

  //
  //    public void testAddEvent_IdAndSequenceNumberInitialized() {
  //        AggregateIdentifier identity = new UUIDAggregateIdentifier();
  //        StubDomainEvent domainEvent = new StubDomainEvent();
  //
  //        EventContainer eventContainer = new EventContainer(identity);
  //        assertEquals(identity, eventContainer.getAggregateIdentifier());
  //        eventContainer.initializeSequenceNumber(11L);
  //
  //        assertEquals(0, eventContainer.size());
  //        assertFalse(eventContainer.getEventStream().hasNext());
  //
  //        eventContainer.addEvent(domainEvent);
  //
  //        assertEquals(1, eventContainer.size());
  //        assertEquals(new Long(12), domainEvent.getSequenceNumber());
  //        assertEquals(identity, domainEvent.getAggregateIdentifier());
  //        assertTrue(eventContainer.getEventStream().hasNext());
  //
  //        eventContainer.clear();
  //
  //        assertEquals(0, eventContainer.size());
  //    }
  //
  //    @Test
  //    public void testAddEventWithId_IdConflictsWithContainerId() {
  //        AggregateIdentifier identity = new UUIDAggregateIdentifier();
  //        StubDomainEvent domainEvent = new StubDomainEvent(identity);
  //
  //        EventContainer eventContainer = new EventContainer(new UUIDAggregateIdentifier());
  //        eventContainer.initializeSequenceNumber(11L);
  //
  //        try {
  //            eventContainer.addEvent(domainEvent);
  //            fail("Expected IllegalArgumentException");
  //        }
  //        catch (IllegalArgumentException e) {
  //            assertTrue(e.getMessage().toLowerCase().contains("identity"));
  //            assertTrue(e.getMessage().toLowerCase().contains("match"));
  //        }
  //    }
  //
  //    @Test
  //    public void testAddEvent_SequenceNumberInitialized() {
  //        AggregateIdentifier identity = new UUIDAggregateIdentifier();
  //        StubDomainEvent domainEvent = new StubDomainEvent(identity);
  //        StubDomainEvent domainEvent2 = new StubDomainEvent(identity);
  //        domainEvent.setSequenceNumber(123);
  //
  //        EventContainer eventContainer = new EventContainer(identity);
  //
  //        eventContainer.addEvent(domainEvent);
  //        eventContainer.addEvent(domainEvent2);
  //
  //        assertEquals(new Long(124), domainEvent2.getSequenceNumber());
  //    }

}