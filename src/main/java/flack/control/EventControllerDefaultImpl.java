package flack.control;

import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;

import flack.commands.api.Command;
import flack.control.api.Dispatcher;
import flack.control.api.Event;
import flack.control.api.EventController;

@Component
@Service
public class EventControllerDefaultImpl implements EventListener, EventController {
	
	@Reference
	Dispatcher dispatcher;
	
	private Map<Class<? extends Event>, Command> commands = new HashMap<Class<? extends Event>, Command>();
	
	public EventControllerDefaultImpl() {
		super();
	}

	/* (non-Javadoc)
	 * @see flack.control.EventController#addCommand(java.lang.Class, flack.commands.api.Command)
	 */
	@Override
	public void addCommand(Class<? extends Event>event, Command command) {
		commands.put(event, command);
		dispatcher.addController(event, (EventController)this);
	}
	
	/* (non-Javadoc)
	 * @see flack.control.EventController#executeCommand(flack.control.EventDefaultImpl)
	 */
	
	public void executeCommand(Event event){
		Command command = commands.get(event.getClass());
		if ( command != null) {
			command.process(event);
		}
	}
	
}
