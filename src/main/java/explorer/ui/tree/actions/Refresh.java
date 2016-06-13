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
package explorer.ui.tree.actions;

import static org.osgi.service.event.EventConstants.EVENT_TOPIC;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.Resource;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import explorer.core.api.ResourceTreeModel;
import explorer.ui.EventTypes;

@SuppressWarnings("serial")
@Component(name = "Sling Explorer Menu Action - Refresh Resource")
@Service(value = { AbstractAction.class, EventHandler.class })
@Properties(value = {
		@Property(name = EVENT_TOPIC, value = EventTypes.VIEW_SELECTION),
		@Property(name = "menuType", value = "TREEMENU") })
public class Refresh extends AbstractAction implements EventHandler {

	private Resource selectedResource;

	@Reference
	ResourceTreeModel treeModel;

	public Refresh() {
		super("Refresh");
	}

	private static final Logger log = LoggerFactory.getLogger(Refresh.class);

	@Override
	public void handleEvent(Event event) {
		selectedResource = (Resource) event.getProperty("data");
		log.trace("received event {}", selectedResource.toString());
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		treeModel.fireStructureChanged(selectedResource);
	}

}
