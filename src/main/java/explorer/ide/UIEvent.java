package explorer.ide;

import java.util.HashMap;

import org.osgi.service.event.Event;

public class UIEvent extends Event {

	
	public UIEvent(String topic, final Object source, final Object data){
		super(topic, new HashMap<String, Object>(){{
			put("source",source);
			put("data",data);
		}});
	}

}
