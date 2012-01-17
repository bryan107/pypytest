package wsnMessageControl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import s2h.util.jms.JmsStub;

public class TarokoEventManager {

	public static final String TOPIC = "ssh.RAW_DATA"; 
	private static Log logger = LogFactory.getLog(TarokoEventManager.class);
	private Map<String, TarokoEvent> evts = new HashMap<String, TarokoEvent>();
	private JmsStub stub;
	
	public TarokoEventManager(JmsStub stub) {
		this.stub = stub;
	}

	public void dispatchEvent(final Event event){
		if (!evts.containsKey(event.getId())) {
			evts.put(event.getId(), new TarokoEvent());
		}
		TarokoEvent tarokoEvent = evts.get(event.getId());
		tarokoEvent.accumulate(event);
		if (tarokoEvent.isCompleted()) {
			sendMessage(tarokoEvent);
		}
	}

	protected void sendMessage(TarokoEvent tarokoEvent) {
	    if (tarokoEvent == null)
        {
            logger.error("Taroko Null");
        }
	    else{
	        logger.info("Taroko Good");
	    }
	    if (stub == null){
	        logger.error("Stub Null");
	    }
	    else{
	        logger.info("Stub Good");
	    }
		try {
			stub.send(tarokoEvent.toJsonFormat(), TOPIC);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		tarokoEvent.recycle();
	}

}
