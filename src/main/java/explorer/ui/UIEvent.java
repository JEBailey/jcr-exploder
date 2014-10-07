package explorer.ui;

import java.util.HashMap;

import org.osgi.service.event.Event;

/**
 * Utility class that wraps the default OSGi event object 
 * and extends it with the most common gui needs for an event
 * 
 * 
 * @author jebailey
 *
 */
public class UIEvent extends Event {

	public UIEvent(String topic, final Object source, final Object data) {
		super(topic, new HashMap<String, Object>() {
			private static final long serialVersionUID = 1L;
			{
				put("source", source);
				put("data", data);
			}
		});
	}
	
	public Object getSource(){
		return getProperty("source");
	}
	
	public Object getData(){
		return getProperty("data");
	}

}
