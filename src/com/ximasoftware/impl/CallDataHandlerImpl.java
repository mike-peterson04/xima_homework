package com.ximasoftware.impl;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import com.ximasoftware.CallDataHandler;
import com.ximasoftware.EventType;

public class CallDataHandlerImpl implements CallDataHandler {
	//I think storing completed calls in a database would be a more scalable solution but for purposes of this example I am storing them in memory
	ConcurrentHashMap<String,Call> activeCalls = new ConcurrentHashMap<String,Call>();
	ConcurrentHashMap<String,Call> completeCalls = new ConcurrentHashMap<String,Call>();
	ConcurrentHashMap<String,Long> callTotals;
	ConcurrentHashMap<String,Party> parties = new ConcurrentHashMap<String,Party>();

	@Override
	public void onCallData(String data) {
		String[] breakDown = data.split(",");
		//check to see if call has already been created
		try {
			if (activeCalls.containsKey(breakDown[0])) {
				//if call is dropping move to completed calls
				Call update = activeCalls.get(breakDown[0]);
				if (breakDown[1].contains("DROP")) {
						activeCalls.remove(breakDown[0]);
						completeCalls.put(breakDown[0], update);
						update.update(breakDown[1]);
						
						this.eventManager(update);	
					
				}
				//updating call status if not dropped
				else {
					update.update(breakDown[1]);
					activeCalls.replace(breakDown[0],update);
					//this.eventManager(update);
					
				}
				
			}
			else {
				Call call = new Call(data);
				if(parties.containsKey(breakDown[2])&& parties.containsKey(breakDown[3])) {
					
					parties.get(breakDown[2]).addCall(call);
					parties.get(breakDown[3]).addCall(call);	
				}
				else if(parties.containsKey(breakDown[2])||parties.containsKey(breakDown[3])) {
					if(parties.containsKey(breakDown[2])){
						parties.get(breakDown[2]).addCall(call);
						parties.put(breakDown[3], new Party(breakDown[3],call));
						}
					else if(parties.containsKey(breakDown[3])) {
						parties.get(breakDown[3]).addCall(call);
						parties.put(breakDown[2], new Party(breakDown[2],call));
						}
					}
				else {
					
					parties.put(breakDown[2], new Party(breakDown[2],call));
					parties.put(breakDown[3], new Party(breakDown[3],call));
					
				}
				
				
				activeCalls.put(breakDown[0], call);
			}
			
		}
		catch(Exception e) {
			System.out.println(e);
			System.out.println(data);
		}
		
		

	}

	@Override
	public int getNumberOfActiveCalls() {
		
		return activeCalls.size();
	}

	@Override
	public int getNumberOfCompletedCalls() {
		return completeCalls.size();
	}

	@Override
	public long getTotalEventDuration(EventType type) {
		if(callTotals == null) {
			return 0;
		}
		try {
			return callTotals.get(type.toString());	
		}
		catch(Exception e){
			System.out.println(type);
			return 0;
		}
		
	}

	@Override
	public long getTotalCallTimeForParty(String party) {
		Party agent = parties.get(party);
		if (agent == null) {
			return 0;
		}
		
		return agent.timeTotal();
	}
	
	private void eventManager(Call updater) {
		//tracking time of completed calls that I think would make more sense to store in a DB as opposed to in memory if this were truly being implemented but keeping in memory for purposes of this assignment
		if (callTotals == null){
			
			long a = 0;
			callTotals = new ConcurrentHashMap<String,Long>();
			callTotals.put("DIAL", a);
			callTotals.put("RING", a);
			callTotals.put("TALK", a);
			callTotals.put("HOLD", a);
			callTotals.put("DROP", a);
			callTotals.put("TOTAL", a);
			
		}
			ConcurrentHashMap<String,Long> updaterCall = updater.activeTime;
			
			updaterCall.forEach(
				    (key, value) -> callTotals.merge( key, value, (v1, v2) ->  v1 + v2)
					);
			long total = 0;
			for(long i : updaterCall.values()) {
				total += i;
			}
			total += callTotals.get("TOTAL");
			callTotals.replace("TOTAL",total);
			
			
	
			
		
	}

}


