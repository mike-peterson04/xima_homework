package com.ximasoftware.impl;

import java.util.HashMap;


public class Call {
	String id;
	String callStatus;
	String callingParty;
	String recievingParty;
	long startTime;
	HashMap<String,Long> activeTime;

	public Call(String callData) {
		String[] breakdown = callData.split(",");
		id = breakdown[0];
		callStatus = breakdown[1];
		callingParty = breakdown[2];
		recievingParty = breakdown[3];
		startTime = System.currentTimeMillis();
		//generating Map in private function for legibility
		this.generateTimeTracker();
		
	}
	
	private void generateTimeTracker() {
		long startTime = 0;
		activeTime = new HashMap<String,Long>();
		activeTime.put("DIAL", startTime);
		activeTime.put("RING", startTime);
		activeTime.put("TALK", startTime);
		activeTime.put("HOLD", startTime);
		activeTime.put("DROP", startTime);
		
	}
	
	public void update(String status) {
		//getting current system time
		long currentTime = System.currentTimeMillis();
		//preserving current call state
		String currentState = callStatus;
		//getting the current states current time
		long stateTime = activeTime.get(currentState);
		//Determine how many MS since startTime and adding any prior events to running total
		stateTime = (currentTime - startTime)+stateTime;
		//updating startTime to last change and status to new status
		startTime = currentTime;
		callStatus = status;
		//updating time tracker
		activeTime.replace(currentState, stateTime);
		
		
		
		
	}

}
