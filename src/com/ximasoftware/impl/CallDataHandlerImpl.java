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
		if (activeCalls.containsKey(breakDown[0])) {
			System.out.println("if");
			System.out.println(data);
			
		}
		else if(activeCalls.containsKey(breakDown[0])) {
			System.out.println("else if");
			System.out.println(data);
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


