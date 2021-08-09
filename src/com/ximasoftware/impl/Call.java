package com.ximasoftware.impl;

import java.util.Hashtable;

public class Call {
	String id;
	String callStatus;
	String callingParty;
	String recievingParty;
	long startTime;
	Hashtable<String,Long> activeTime;

	public Call(String callData) {
		String[] breakdown = callData.split(",");
		id = breakdown[0];
		callStatus = breakdown[1];
		callingParty = breakdown[2];
		recievingParty = breakdown[3];
		startTime = System.currentTimeMillis();
		//generating table in private function for legibility
		this.generateTimeTracker();
		
	}
	
	private void generateTimeTracker() {
		long startTime = 0;
		activeTime = new Hashtable<String,Long>();
		activeTime.put("DIAL", startTime);
		activeTime.put("RING", startTime);
		activeTime.put("TALK", startTime);
		activeTime.put("HOLD", startTime);
		activeTime.put("DROP", startTime);
		
	}
	
	public void update(String status) {
		
	}

}
