package com.apm4all.tracy;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class TracyTest {
	static final String TASK_ID = "TID-ab1234-x";
	static final String PARENT_OPT_ID = "1234";
	static final String L1_LABEL_NAME = "L1 Operation";
	static final String L11_LABEL_NAME = "L11 Operation";
	

	@Test
	public void testSetContext_empty() {
		Tracy.setContext();
		assertEquals(Tracy.TRACY_DEFAULT_TASK_ID, Tracy.getTaskId());
		assertEquals(Tracy.TRACY_DEFAULT_PARENT_OPT_ID, Tracy.getParentOptId());
	}

	@Test
	public void testSetContext_full() {
		Tracy.setContext(TASK_ID, PARENT_OPT_ID);
		assertEquals(TASK_ID, Tracy.getTaskId());
		assertEquals(PARENT_OPT_ID, Tracy.getParentOptId());
	}
	
	@Test
	public void testGetEvents() throws InterruptedException {
		Tracy.setContext(TASK_ID, PARENT_OPT_ID);
		Tracy.before(L1_LABEL_NAME);
		Thread.sleep(100);
		Tracy.after(L1_LABEL_NAME);
		List<TracyEvent> events = Tracy.getEvents();
		assertEquals(1, events.size());
		TracyEvent event = events.get(0);
		assertEquals(TASK_ID, event.getTaskId());
		assertEquals(PARENT_OPT_ID, event.getParentOptId());
		assertEquals(L1_LABEL_NAME, event.getLabel());
	}
	
	@Test
	public void testGetEvents_twoEventsTwoLevelStack() throws InterruptedException {
		Tracy.setContext(TASK_ID, PARENT_OPT_ID);
		Tracy.before(L1_LABEL_NAME);
		Tracy.before(L11_LABEL_NAME);
		Thread.sleep(100);
		Tracy.after(L11_LABEL_NAME);
		Tracy.after(L1_LABEL_NAME);
		List<TracyEvent> events = Tracy.getEvents();
		assertEquals(2, events.size());
	
		// L1 event will be popped last
		TracyEvent l1Event = events.get(1);
		assertEquals(TASK_ID, l1Event.getTaskId());
		assertEquals(PARENT_OPT_ID, l1Event.getParentOptId());
		assertEquals(L1_LABEL_NAME, l1Event.getLabel());
		
		// L11 event will be popped first
		TracyEvent l11Event = events.get(0);
		assertEquals(TASK_ID, l11Event.getTaskId());
		assertEquals(l1Event.getOptId(), l11Event.getParentOptId());
		assertEquals(L11_LABEL_NAME, l11Event.getLabel());
	}
}
