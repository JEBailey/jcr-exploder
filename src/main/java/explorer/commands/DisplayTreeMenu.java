package explorer.commands;

import java.awt.Point;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;

import explorer.ide.tree.RightClickMenu;
import flack.commands.api.Command;
import flack.control.api.Event;

@Component(name="Sling Explorer Command - Display Tree Menu",description="Provides the UI for Tree Options")
@Service
@Property(name="type", value="displayMenu")
public class DisplayTreeMenu implements Command {

	@Reference
	RightClickMenu menu;
	
	@Override
	public void process(Event event) {
		Point e = (Point)event.getData();
		menu.show((java.awt.Component) event.getSource(),(int) e.getX(),(int) e.getY());
	}

}
