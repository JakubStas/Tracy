package com.apm4all.tracy;
import java.util.List;
import org.databene.contiperf.*;
import org.databene.contiperf.junit.ContiPerfRule;
import org.junit.Rule;
import org.junit.Test;

@PerfTest(threads=10, duration=3000, rampUp = 100)
public class TracyTestPerf {
	static final String TASK_ID = "TID-ab1234-x";
	static final String PARENT_OPT_ID = "AAAA";
	static final String L1_LABEL_NAME = "L1 Operation";
	static final String L11_LABEL_NAME = "L11 Operation";
	@Rule
	public ContiPerfRule i = new ContiPerfRule();

	@Required(average = 1, percentile99=1, max = 20)
	@Test
	public void singleTraceTest() {
		Tracy.setContext(TASK_ID, PARENT_OPT_ID);
		Tracy.before("L1");
		  Tracy.annotate("someKey", "someValue");
          Tracy.before("L11");
		  Tracy.annotate("someKey", "someValue");
		  Tracy.after("L11");
		  Tracy.before("L12");
		  Tracy.annotate("someKey", "someValue");
		  Tracy.after("L12");
		  Tracy.before("L13");
		  Tracy.annotate("someKey", "someValue");
		  Tracy.after("L13");
		Tracy.after("L1");
		List<TracyEvent> events = Tracy.getEvents();
		events.get(0);
	}
}
