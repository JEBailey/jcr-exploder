package explorer.ui;

import java.util.HashMap;

import org.osgi.service.event.Event;

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

}
