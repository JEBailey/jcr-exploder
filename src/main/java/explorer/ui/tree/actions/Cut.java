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

import explorer.ui.EventTypes;
import explorer.ui.ResourceClipboardBuffer;

@SuppressWarnings("serial")
@Component(name = "Sling Explorer Menu Action - Cut Node", description = "Move a node to a new location")
@Service(value = { AbstractAction.class, EventHandler.class })
@Properties(value = { @Property(name = EVENT_TOPIC, value = EventTypes.NEW_SELECTION),
		@Property(name = "menuType", value = "TREEMENU") })
public class Cut extends AbstractAction implements EventHandler {

	private Resource selectedResource;
	
	@Reference
	private ResourceClipboardBuffer buffer;

	public Cut() {
		super("Cut");
	}
	
	private static final Logger log = LoggerFactory.getLogger(Cut.class);
	
	@Override
	public void actionPerformed(ActionEvent event) {
		buffer.setResourceToMove(selectedResource);
	}

	@Override
	public void handleEvent(Event event) {
		selectedResource = (Resource) event.getProperty("data");
	}
}
