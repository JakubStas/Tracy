package com.apm4all.tracy;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.Random;

public class TracyThreadContext {
	private static Random r = new Random();
	private String taskId;
	private String parentOptId;
	private Stack<TracyEvent> stack;
	private List<TracyEvent> poppedList;
	//TODO: Stack of TracyEvent(s)
	//TODO: List of popped TracyEvent(s)
	//TODO: Capture hostname
	//TODO: Operation name to be captured as label of Tracy.Before() and TracyAfter
	//TODO: Component name would be useful to gather (1 for the whole context)
	//TODO: Consider mechanism to relay TaskId and parentOptId to child worker threads as well as getting worker thread events back to main thread 
	
	public TracyThreadContext(String taskId, String parentOptId) {
		super();
		this.taskId = taskId;
		this.parentOptId = parentOptId;
		stack = new Stack<TracyEvent>();
		poppedList = new ArrayList<TracyEvent>();
	}

	private int randomNumber(int upperLimit)	{
		return r.nextInt(upperLimit);
	}
	
	private String generateRandomOptId()	{
		return Integer.toString(randomNumber(100));
	}

	public String getTaskId() {
		return taskId;
	}

	public String getParentOptId() {
		return parentOptId;
	}

	public void push(String label) {
		long msec = System.currentTimeMillis();
		// Generate random optId (unique per taskid event set)
		String optId = generateRandomOptId();
		// Create new TracyEvent
		TracyEvent event = new TracyEvent(label, optId, msec);
		stack.add(event);
	}

	public void pop() {
		stack.peek();
		// TODO Auto-generated method stub
		
	}
}
