package com.pku.wireless;

import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import es.libresoft.openhealth.Agent;
import es.libresoft.openhealth.events.Event;
import es.libresoft.openhealth.events.EventType;
import es.libresoft.openhealth.events.InternalEventManager;
import es.libresoft.openhealth.events.MeasureReporter;

public class EventAgent implements InternalEventManager {
	@Override
	public void receivedMeasure(Agent agent, MeasureReporter mr) {
		List<Object> measures = mr.getMeasures();
		Iterator<Object> ims = measures.iterator();

		List<Object> attributes = mr.getAttributes();
		Iterator<Object> iat = attributes.iterator();

		if (!measures.isEmpty()) {
			System.out.println("Measures received from: " + agent.getId());
			while (ims.hasNext()) {
				System.out.println("" + ims.next());
			}
		}

		if (!attributes.isEmpty()) {
			System.out.println("Attributes received from: " + agent.getId());
			while (iat.hasNext()) {
				System.out.println("" + iat.next());
			}
		}
		
	}

	@Override
	public void agentChangeState(Agent agent, int stateCode,
			String stateName) {
		System.out.println("ID: " + agent.getId() + " state: " + stateName);
		if(stateName == "Operating"){
			Timer timer = new Timer();
			timer.schedule(new MyTask(agent), 2000);
		}
	}
	
	class MyTask extends TimerTask{
		private Agent agent;
		public MyTask(Agent agent){
			this.agent=agent;
		}
		@Override
		public void run() {	
			agent.sendEvent(new Event(EventType.NOTI_MEASURE));
		}
	}

	@Override
	public void agentPlugged(Agent agent) {
		System.out.println("Agent " + agent.getId() + " connected");
		
	}

	@Override
	public void agentUnplugged(Agent agent) {
		System.out.println("Agent " + agent.getId() + " disconnected");
	}

	@Override
	public void error(Agent agent, int errorCode) {
		// TODO Auto-generated method stub
		
	}
}
