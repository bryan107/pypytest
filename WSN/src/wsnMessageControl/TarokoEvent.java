package wsnMessageControl;

import java.util.HashMap;
import java.util.Map;

import s2h.util.json.JsonUtils;

@SuppressWarnings("serial")
public class TarokoEvent
{

    public final static String TYPE = "taroko";

    private long id = -1;

    private Map<String, String> packet = new HashMap<String, String>() {
        {
            put("type", TYPE);
            put("humidity", "0");
            put("temperature", "0");
        }
    };

    public boolean isCompleted()
    {
        boolean hasHumidity = !"0".equals(packet.get("humidity"));
        boolean hasTemperature = !"0".equals(packet.get("temperature"));
        return hasHumidity && hasTemperature;
    }

    public boolean accumulate(Event event)
    {
        if (id == -1)
        {
            id = Long.valueOf(event.getId());
            packet.put("id", "" + id);
        }

        if (id != Long.valueOf(event.getId()))
        {
            return false;
        }

        if ("temperature".equalsIgnoreCase(event.getAttribute()))
        {
            packet.put("temperature", event.getValue());
        }

        if ("humidity".equalsIgnoreCase(event.getAttribute()))
        {
            packet.put("humidity", event.getValue());
        }
        return isCompleted();
    }

    public String toJsonFormat()
    {
        return JsonUtils.createBuilder().toJson(packet);
    }

    public void recycle()
    {
        packet.put("humidity", "0");
        packet.put("temperature", "0");
    }

    public String toString()
    {
        return String.format("TarokoEvent[type=%s,id=%d,temp=%s,humi=%s] = %s", TYPE, id, packet.get("temperature"), packet.get("humidity"));
    }

    // private String humidity;
    // private String t;

    // System.out.println(getStub());
    // getStub().send(
    // JsonUtils.createBuilder().add("type", "taroko").add("id",
    // message[0]).add("humidity", message[2]).add(
    // "temperature", message[1]).toJson(), "ssh.RAW_DATA");
}
