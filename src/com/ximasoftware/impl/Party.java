package com.ximasoftware.impl;

import java.util.ArrayList;


public class Party {
	String id;
	ArrayList<Call> calls = new ArrayList<Call>();
	
	public Party(String partyId, Call call) {
		id = partyId;
		calls.add(call);
	}
	
	public void addCall(Call call) {
		calls.add(call);
	}
	
	public long timeTotal() {
		long total = 0;
		for (int i = 0; i < calls.size();i++) {
			total += calls.get(i).totalTime;
		}
		return total;
	}
}
