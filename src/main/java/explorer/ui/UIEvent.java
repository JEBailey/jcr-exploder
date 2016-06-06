/*
Copyright 2016 JE Bailey

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
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
