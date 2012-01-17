package light_control_filtered;

import java.util.Map;

import junit.framework.TestCase;
import s2h.util.json.JsonUtils;

public class ReceiverTest extends TestCase {

	Receiver receiver;
	int[] locData = new int[5];

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		receiver = new Receiver(null, locData);
	}

	public void testRelocate() throws Exception {

		String json = null;

		json = JsonUtils.createBuilder().toJson("peoplelocation", "DOOR");
		receiver.relocate(json);
		assertEquals(1, receiver.peopleLocation[0]);
		assertTrue(locData.equals(receiver.peopleLocation));

		json = JsonUtils.createBuilder().toJson("peoplelocation", "LIVINGROOM");
		receiver.relocate(json);
		assertEquals(1, receiver.peopleLocation[1]);
		assertTrue(locData.equals(receiver.peopleLocation));

		json = JsonUtils.createBuilder().toJson("peoplelocation", "KITCHEN");
		receiver.relocate(json);
		assertEquals(1, receiver.peopleLocation[2]);
		assertTrue(locData.equals(receiver.peopleLocation));
		assertTrue(locData.equals(receiver.peopleLocation));

		json = JsonUtils.createBuilder().toJson("peoplelocation", "BEDROOM");
		receiver.relocate(json);
		assertEquals(1, receiver.peopleLocation[3]);
		assertTrue(locData.equals(receiver.peopleLocation));

		json = JsonUtils.createBuilder().toJson("peoplelocation", "TOILET");
		receiver.relocate(json);
		assertEquals(1, receiver.peopleLocation[4]);
		assertTrue(locData.equals(receiver.peopleLocation));

	}

	public void testJson() throws Exception {
		System.out.println(JsonUtils.createBuilder().add("a", "b").add("b", "c").toJson());
		Map<String, String> jsonMap = JsonUtils.get("{\"b\":\"c\",\"a\":\"b\"}");
		System.out.println(jsonMap);
		System.out.println(JsonUtils.createBuilder().add(jsonMap).toJson());
	}

}
