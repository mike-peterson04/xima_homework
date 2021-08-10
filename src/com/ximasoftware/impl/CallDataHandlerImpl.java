package com.ximasoftware.impl;

import java.util.*;
import com.ximasoftware.CallDataHandler;
import com.ximasoftware.EventType;

public class CallDataHandlerImpl implements CallDataHandler {
	//I think storing completed calls in a database would be a more scalable solution but for purposes of this example I am storing them in memory
	HashMap<String,Call> activeCalls = new HashMap<String,Call>();
	HashMap<String,Call> completeCalls = new HashMap<String,Call>();
	HashMap<String,Long> callTotals;

	@Override
	public void onCallData(String data) {
		String[] breakDown = data.split(",");
		//check to see if call has already been created
		if (activeCalls.containsKey(breakDown[0])) {
			//if call is dropping move to completed calls
			if (breakDown[1].contains("DROP")) {
				if(activeCalls.containsKey(breakDown[0])) {
					completeCalls.put(breakDown[0], activeCalls.remove(breakDown[0]));
					completeCalls.get(breakDown[0]).update(breakDown[1]);
					this.eventManager(completeCalls.get(breakDown[0]));	
				}
				
			}
			//updating call status if not dropped
			else {
				activeCalls.get(breakDown[0]).update(breakDown[1]);
				this.eventManager(activeCalls.get(breakDown[0]));
				
			}
			
		}
		else {
			activeCalls.put(breakDown[0], new Call(data));
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
		// TODO Auto-generated method stub
		return 0;
	}
	
	private void eventManager(Call updater) {
		//tracking time of completed calls that I think would make more sense to store in a DB as opposed to in memory if this were truly being implemented but keeping in memory for purposes of this assignment
		if (callTotals == null){
			
			long a = 0;
			callTotals = new HashMap<String,Long>();
			callTotals.put("DIAL", a);
			callTotals.put("RING", a);
			callTotals.put("TALK", a);
			callTotals.put("HOLD", a);
			callTotals.put("DROP", a);
			callTotals.put("TOTAL", a);
			
		}
		else {
			HashMap<String,Long> updaterCall = updater.activeTime;
			
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

}


