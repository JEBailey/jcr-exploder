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

import explorer.ide.EventTypes;
import explorer.ide.tree.RightClickMenu;

@Component(name="Sling Explorer Command - Display Tree Menu",description="Provides the UI for Tree Options")
@Service
@Properties(value={
		@Property(name=EVENT_TOPIC, value=EventTypes.SHOW_TREE_MENU)
})
public class DisplayTreeMenu implements EventHandler {

	@Reference
	RightClickMenu menu;

	@Override
	public void handleEvent(Event event) {
		Point e = (Point)event.getProperty("data");
		java.awt.Component source = (java.awt.Component)event.getProperty("source");
		menu.show(source,(int) e.getX(),(int) e.getY());
	}

}
