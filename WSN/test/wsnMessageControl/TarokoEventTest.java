package wsnMessageControl;

import java.util.Map;

import s2h.util.json.JsonUtils;

import junit.framework.TestCase;

public class TarokoEventTest extends TestCase {

	public void testIsCompletedAndRecycle() {
		Event temperature = new Event(1, 1, 5678);
		Event humidity = new Event(1, 2, 5678);
		TarokoEvent event = new TarokoEvent();
		//
		assertFalse(event.isCompleted());
		//
		event.accumulate(temperature);
		assertFalse(event.isCompleted());
		//
		event.accumulate(humidity);
		assertTrue(event.isCompleted());
		//
		event.recycle();
		assertFalse(event.isCompleted());
	}

	public void testToJsonFormat() {
		Event temperature = new Event(1, 1, 5678);
		Event humidity = new Event(1, 2, 5678);
		TarokoEvent event = new TarokoEvent();
		//
		event.accumulate(temperature);
		event.accumulate(humidity);
		assertTrue(event.isCompleted());
		//
		Map<String, String> json = JsonUtils.get(event.toJsonFormat());
		assertEquals(json.get("id"), "" + temperature.getId());
		assertEquals(json.get("id"), "" + humidity.getId());
		
		assertEquals(json.get("humidity"), humidity.getValue());
		assertEquals(json.get("temperature"), temperature.getValue());
	}


}
