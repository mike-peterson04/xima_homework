package com.ximasoftware.impl;

import java.util.concurrent.ConcurrentHashMap;


public class Call {
	String id;
	String callStatus;
	String callingParty;
	String recievingParty;
	long startTime;
	long changeTime=0;
	long totalTime = 0;
	ConcurrentHashMap<String,Long> activeTime;

	public Call(String callData) {
		String[] breakdown = callData.split(",");
		id = breakdown[0];
		callStatus = breakdown[1];
		callingParty = breakdown[2];
		recievingParty = breakdown[3];
		startTime = System.currentTimeMillis();
		changeTime = startTime;
		//generating Map in private function for legibility
		this.generateTimeTracker();
		
	}
	
	private void generateTimeTracker() {
		long startTime = 0;
		activeTime = new ConcurrentHashMap<String,Long>();
		activeTime.put("DIAL", startTime);
		activeTime.put("RING", startTime);
		activeTime.put("TALK", startTime);
		activeTime.put("HOLD", startTime);
		activeTime.put("DROP", startTime);
		
	}
	
	public void update(String status) {
		//getting current system time
		long currentTime = System.currentTimeMillis();
		totalTime = currentTime-startTime;
		//preserving current call state
		String currentState = callStatus;
		//getting the current states current time
		long stateTime = activeTime.get(currentState);
		//Determine how many MS since startTime and adding any prior events to running total
		stateTime = (currentTime - changeTime)+stateTime;
		//updating startTime to last change and status to new status
		changeTime = currentTime;
		callStatus = status;
		//updating time tracker
		activeTime.replace(currentState, stateTime);
		
		
		
		
	}

}
