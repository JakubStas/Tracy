/*
 * Copyright 2014 Joao Vicente
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.apm4all.tracy;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.Random;

public class TracyThreadContext {
	private static Random r = new Random();
	private static String hostname = null;
	private String taskId;
	private String parentOptId;
	private Stack<TracyEvent> stack;
	private List<TracyEvent> poppedList;
	//TODO: Devise mechanism to relay TaskId and parentOptId to child worker threads as well as getting worker thread events back to main thread 
	//TODO: Develop TracyEvent log reporter 
	
	public TracyThreadContext(String taskId, String parentOptId) {
		super();
		resolveHostname();
		this.taskId = taskId;
		this.parentOptId = parentOptId;
		stack = new Stack<TracyEvent>();
		poppedList = new ArrayList<TracyEvent>();
	}

	static void resolveHostname() {
		try {
			if (hostname == null) {
				hostname = InetAddress.getLocalHost().getHostName();
			}
		} catch (UnknownHostException e) {
			hostname = "unknown";
		}
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
		String eventParentOptId;
		// Event parent OptId will be lower stack event,
		if (stack.size() > 0)	{
			 eventParentOptId = stack.peek().getOptId();
		}
		else	{
			// or context parent in case this is first stack level
			eventParentOptId = this.parentOptId;
		}
		// Generate random optId (must be unique per taskid event set)
		String optId = generateRandomOptId();
		// Create new TracyEvent
		TracyEvent event = new TracyEvent(this.taskId, label, eventParentOptId, optId, msec);
		event.addAnnotation("Host", hostname);
		stack.add(event);
	}

	public void pop() {
		TracyEvent event = stack.pop();
		event.setMsecAfter(System.currentTimeMillis());
		poppedList.add(event);
	}

	public List<TracyEvent> getPoppedList() {
		return poppedList;
	}

	public void setPoppedList(List<TracyEvent> poppedList) {
		this.poppedList = poppedList;
	}
	
	public void annotate(String... args) {
		stack.peek().addAnnotations(args);
	}
}
