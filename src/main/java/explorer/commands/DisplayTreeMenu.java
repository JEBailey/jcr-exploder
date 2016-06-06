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
package explorer.commands;

import static org.osgi.service.event.EventConstants.EVENT_TOPIC;

import java.awt.Point;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import explorer.ui.EventTypes;
import explorer.ui.tree.RightClickMenu;

@Component(name = "Sling Explorer Command - Display Tree Menu")
@Service
@Properties(value = { @Property(name = EVENT_TOPIC, value = EventTypes.SHOW_TREE_MENU) })
public class DisplayTreeMenu implements EventHandler {

	@Reference
	RightClickMenu menu;

	@Override
	public void handleEvent(Event event) {
		Point e = (Point) event.getProperty("data");
		java.awt.Component source = (java.awt.Component) event
				.getProperty("source");
		menu.show(source, (int) e.getX(), (int) e.getY());
	}

}
