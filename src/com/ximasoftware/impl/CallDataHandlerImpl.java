package com.ximasoftware.impl;

import java.util.*;
import com.ximasoftware.CallDataHandler;
import com.ximasoftware.EventType;

public class CallDataHandlerImpl implements CallDataHandler {
	//I think storing completed calls in a database would be a more scalable solution but for purposes of this example I am storing them in memory
	Hashtable<String,Call> activeCalls = new Hashtable<String,Call>();
	Hashtable<String,Call> completeCalls = new Hashtable<String,Call>();

	@Override
	public void onCallData(String data) {
		String[] breakDown = data.split(",");
		//check to see if call has already been created
		if (activeCalls.containsKey(breakDown[0])) {
			//if call is dropping move to completed calls
			if (breakDown[1]=="DROP") {
				completeCalls.put(breakDown[0], activeCalls.remove(breakDown[0]));
				completeCalls.get(breakDown[0]).update(breakDown[1]);
			}
			//updating call status if not dropped
			else {
				activeCalls.get(breakDown[0]).update(breakDown[1]);
				
			}
			
		}
		else {
			activeCalls.put(breakDown[0], new Call(data));
		}

	}

	@Override
	public int getNumberOfActiveCalls() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNumberOfCompletedCalls() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getTotalEventDuration(EventType type) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getTotalCallTimeForParty(String party) {
		// TODO Auto-generated method stub
		return 0;
	}

}


