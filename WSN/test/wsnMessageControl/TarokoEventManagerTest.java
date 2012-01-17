package wsnMessageControl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import s2h.util.json.JsonUtils;

import junit.framework.TestCase;

public class TarokoEventManagerTest extends TestCase {

	public void testDispatchEvent() {
		
		final List<TarokoEvent> events = new ArrayList<TarokoEvent>();
		
		TarokoEventManager mgr = new TarokoEventManager(null) {
			@Override
			protected void sendMessage(TarokoEvent tarokoEvent) {
				events.add(tarokoEvent);
			}
		};
		Event temperature = new Event(1, 1, 5678);
		Event humidity = new Event(1, 2, 5678);
		mgr.dispatchEvent(humidity);
		assertTrue(events.isEmpty());
		mgr.dispatchEvent(temperature);
		assertFalse(events.isEmpty());
		//
		// validate the final result.
		TarokoEvent evt = events.get(0);
		Map<String, String> json = JsonUtils.get(evt.toJsonFormat());
		System.out.println(" ID= " + json.get("id") + " humi= " +  json.get("humidity") + " temp= " + json.get("temperature"));
		System.out.println("tempid = " + temperature.getId() + " tempvalue = " + temperature.getValue() + " humiid = " + humidity.getId() + " humivalue = " + humidity.getValue());
		assertEquals(json.get("id"), "" + temperature.getId());
		assertEquals(json.get("id"), "" + humidity.getId());
		
		assertEquals(json.get("humidity"), humidity.getValue());
		assertEquals(json.get("temperature"), temperature.getValue());		
	}

}
