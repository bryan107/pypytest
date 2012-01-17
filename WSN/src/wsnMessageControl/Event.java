package wsnMessageControl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import s2h.util.json.JsonUtils;
import wsnMessageControl.transform.Transformer;

public class Event {

	private static Log logger = LogFactory.getLog(Event.class);
	//
	private long rawAttribute;
	private long rawValue;
	private long id;
	//
	private String datetime;
	
	private static SimpleDateFormat FMT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	protected Event(long id, long attribute, long value) {
		super();
		this.id = id;
		this.rawAttribute = attribute;
		this.rawValue = value;
		this.datetime = FMT.format(new Date());
	}
	
	public String toString() {
		return String.format("Event[id=%d,attr=%s,time=%s,loc=%s] = %s", id,
				getAttribute(), getTime(), getLocation(), getValue());
	}
	
	public static void main(String[] args) {
		System.out.println(new Event(1, 1, 1234));
	}
	
	public static Event decode(byte[] data){
		try {
			int ptr = 0;
			long id = toLong(data, ptr, data[ptr]);
			ptr += (data[ptr] + 1);
			
			long attribute = toLong(data, ptr, data[ptr]);
			ptr += (data[ptr] + 1);
			
			long value = toLong(data, ptr, data[ptr]);
			
			return new Event(id, attribute, value);
		} catch (Exception warned) {
			logger.warn(warned.getMessage(), warned);
		}
		
		return null;
	}
	
	public String getAttribute(){
		return Transformer.getInstance().getSensorAttribute(rawAttribute);
	}
	
	public String getLocation(){
		return Transformer.getInstance().getLocation(id);
	}
	
	public String getValue(){
		return Transformer.getInstance().getSensorValue(getAttribute(), rawValue);
	}
	
	public String getTime(){
		return datetime;
	}
	
	public String toJsonFormat(){
		Map<String, String> json = new HashMap<String, String>();
		json.put("id", getId());
		json.put("attribute", getAttribute());
		json.put("location", getLocation());
		json.put("value", getValue());
		json.put("time", getTime());
		return JsonUtils.createBuilder().add(json).toJson();
	}
	
	private static long toLong(byte[] data, final int offset, int length) {
		final int _offset = offset + 1;
		long value = 0;
		for (int i = 0; i < length; i++) {
			int shift = (length - 1 - i) * 8;
			value += (data[i + _offset] & 0x000000FF) << shift;
		}
		return value;
	}

	public String getId() {
		String id = "" +this.id;
		return id;
	}

}
