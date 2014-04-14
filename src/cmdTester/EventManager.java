package cmdTester;

import java.util.Timer;
import java.util.TimerTask;

import Config.BloodPressureMeasure;
import Config.PulseMeasure;
import es.libresoft.openhealth.Agent;
import es.libresoft.openhealth.events.Event;
import es.libresoft.openhealth.events.EventType;
import es.libresoft.openhealth.events.InternalEventManager;
import es.libresoft.openhealth.events.MeasureReporter;
import es.libresoft.openhealth.logging.Logging;

public class EventManager implements InternalEventManager{
	@Override
	public void agentChangeState(Agent agent, int stateCode, String stateName) {
		Logging.debug("ID: " + agent.getId() + " stateCode: " + stateCode + " stateName: " + stateName);
		if(stateName == "Operating"){
			Timer timer = new Timer();
			timer.schedule(new MyTask(agent), 1000);
		}
	}
	class MyTask extends TimerTask{
		private Agent agent;
		public MyTask(Agent agent){
			this.agent=agent;
		}
		@Override
		public void run() {	
			agent.sendEvent(new Event(EventType.REQ_MDS));
		}
	}
	@Override
	public void receivedMeasure(Agent agent, MeasureReporter mr) {
		Logging.debug(agent.getId()+":");
		MeasureDataDecoder measureDataDecoder = new MeasureDataDecoder();
		measureDataDecoder.setMeasureReporter(mr);
		measureDataDecoder.init();
		measureDataDecoder.receiveMeasures();
	}

	@Override
	public void agentPlugged(Agent agent) {
		Logging.debug("TODO: agentPlugged");
	}

	@Override
	public void agentUnplugged(Agent agent) {
		Logging.debug("TODO: agentUnplugged");
	}

	@Override
	public void error(Agent agent, int errorCode) {
		Logging.debug("TODO: error: " + errorCode);
	}

};

